package org.ncic.bioinfo.sparkseq.processes.variantcalling

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.bundle.{RefPartitionInfoBundle, SAMBundle, VCFBundle}
import org.ncic.bioinfo.sparkseq.data.common.{RefPartitionInfo, SamHeaderInfo, VcfHeaderInfo}
import org.ncic.bioinfo.sparkseq.data.partition.{BundlePartition, FastaPartition, SamRecordPartition, VcfRecordPartition}
import org.ncic.bioinfo.sparkseq.exceptions.PipelineException
import org.apache.spark.SparkContext._
import org.ncic.bioinfo.sparkseq.algorithms.adapters.HaplotypeCallerAdapter
import org.ncic.bioinfo.sparkseq.algorithms.walker.SerializableActiveRegionMapData
import org.ncic.bioinfo.sparkseq.const.BinTools
import org.ncic.bioinfo.sparkseq.debug.Dumper

import collection.JavaConversions._
import scala.collection.mutable.ListBuffer

/**
  * Author: wbc
  */
object HaplotypeCallerProcess {
  def apply(name: String,
            referencePath: String,
            rodMap: Map[String, String],
            refPartitionInfoBundle: RefPartitionInfoBundle,
            inputSamBundleList: List[SAMBundle],
            outputVCFBundle: VCFBundle,
            useGVCF: Boolean,
            samHeaderInfo: SamHeaderInfo,
            vcfHeaderInfo: VcfHeaderInfo): HaplotypeCallerProcess = {
    val inputSamBundleMap = inputSamBundleList.map(samBundle => (samBundle.key, samBundle)).toMap
    apply(name, referencePath, rodMap, refPartitionInfoBundle, inputSamBundleMap, outputVCFBundle, useGVCF,
      samHeaderInfo, vcfHeaderInfo);
  }

  def apply(name: String,
            referencePath: String,
            rodMap: Map[String, String],
            refPartitionInfoBundle: RefPartitionInfoBundle,
            inputSAMBundleMap: Map[String, SAMBundle],
            outputVCFBundle: VCFBundle,
            useGVCF: Boolean,
            samHeaderInfo: SamHeaderInfo,
            vcfHeaderInfo: VcfHeaderInfo): HaplotypeCallerProcess = {
    new HaplotypeCallerProcess(name, referencePath, rodMap,
      refPartitionInfoBundle, inputSAMBundleMap, outputVCFBundle, useGVCF, samHeaderInfo, vcfHeaderInfo)
  }
}

class HaplotypeCallerProcess(name: String,
                             referencePath: String,
                             rodMap: Map[String, String],
                             refPartitionInfoBundle: RefPartitionInfoBundle,
                             inputSAMBundleMap: Map[String, SAMBundle],
                             outputVCFBundle: VCFBundle,
                             useGVCF: Boolean,
                             samHeaderInfo: SamHeaderInfo,
                             vcfHeaderInfo: VcfHeaderInfo)
  extends VariantCallingProcess(name, referencePath, rodMap, refPartitionInfoBundle, inputSAMBundleMap, outputVCFBundle) {

  override def getVCFRecords(bundleRDD: RDD[BundlePartition]): RDD[VcfRecord] = {

    val useGVCFValue = sc.broadcast(useGVCF).value
    val refContigInfoValue = sc.broadcast(refPartitionInfoBundle.refPartitionInfo.refContigInfo).value
    val samHeaderValue = sc.broadcast(samHeaderInfo).value
    val vcfHeaderValue = sc.broadcast(vcfHeaderInfo).value

    val rodKeysBD = sc.broadcast(rodMap.keys).value
    bundleRDD.flatMap(bundle => {
      try {
        val partitionId = bundle.partitionId
        val refPartition = bundle.fastaPartition
        val rodList = rodKeysBD.map(key => bundle.rodPartitionMap.get(key).get).toList

        val mapDataBuffer = ListBuffer[SerializableActiveRegionMapData]()
        bundle.samRecordPartitionMap.foreach(samRecordPartitionWithKey => {
          val key = samRecordPartitionWithKey._1
          val samRecordPartition = samRecordPartitionWithKey._2
          mapDataBuffer ++= HaplotypeCallerAdapter.getActiveRegions(
            bundle.refContigInfo, samRecordPartition, bundle.fastaPartition, rodList, useGVCFValue)
        })
        mapDataBuffer
      } catch {
        case e: Exception => {
          Dumper.dumpBundle(bundle, Dumper.defaultString)
          throw e
        }
      }
    })
      .filter(mapData => mapData.reads.size() > 0)
      //.repartition(BinTools.repartitionCount)
      .mapPartitions(mapDataList => {
        HaplotypeCallerAdapter.callVariants(refContigInfoValue, mapDataList.toList, false, samHeaderValue, vcfHeaderValue)
          .iterator
      })
  }
}
