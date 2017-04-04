package org.ncic.bioinfo.sparkseq.processes.variantcalling

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.algorithms.adapters.MutectAdapter
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.bundle.{RefPartitionInfoBundle, SAMBundle, VCFBundle}
import org.ncic.bioinfo.sparkseq.data.common.{Locus, RefPartitionInfo, SamHeaderInfo}
import org.ncic.bioinfo.sparkseq.data.partition.{BundlePartition, SamRecordPartition}
import org.ncic.bioinfo.sparkseq.debug.Dumper

import collection.JavaConversions._

/**
  * Author: wbc
  */
object MutectProcess {
  def apply(name: String,
            referencePath: String,
            rodMap: Map[String, String],
            refPartitionInfoBundle: RefPartitionInfoBundle,
            tumorSamBundle: SAMBundle,
            normalSamBundle: SAMBundle,
            outputVCFBundle: VCFBundle,
            intervals: List[Locus]): MutectProcess = {
    new MutectProcess(name, referencePath, rodMap,
      refPartitionInfoBundle, tumorSamBundle, normalSamBundle, outputVCFBundle, intervals)
  }
}

class MutectProcess(name: String,
                    referencePath: String,
                    rodMap: Map[String, String],
                    refPartitionInfoBundle: RefPartitionInfoBundle,
                    tumorSamBundle: SAMBundle,
                    normalSamBundle: SAMBundle,
                    outputVCFBundle: VCFBundle,
                    intervals: List[Locus])
  extends VariantCallingProcess(name, referencePath, rodMap, refPartitionInfoBundle,
    List(tumorSamBundle, normalSamBundle).map(samBundle => (samBundle.key, samBundle)).toMap,
    outputVCFBundle) {

  override def getVCFRecords(bundleRDD: RDD[BundlePartition]): RDD[VcfRecord] = {

    val refPartitionInfo = refPartitionInfoBundle.refPartitionInfo
    val intervalsValue = sc.broadcast(intervals).value
    val tumorSamKeyValue = sc.broadcast(tumorSamBundle.key).value
    val normalSamKeyValue = sc.broadcast(normalSamBundle.key).value
    val refContigInfoValue = sc.broadcast(refPartitionInfo.getRefContigInfo).value

    val rodKeysBD = sc.broadcast(rodMap.keys).value
    bundleRDD.flatMap(bundle => {
      try {
        val partitionId = bundle.partitionId
        val refPartition = bundle.fastaPartition
        val rodList = rodKeysBD.map(key => bundle.rodPartitionMap.get(key).get).toList

        val tumorSamPartition = bundle.samRecordPartitionMap.get(tumorSamKeyValue).getOrElse(
          SamRecordPartition.empty(partitionId, refPartition.contigId, SamHeaderInfo.sortedHeader(refContigInfoValue, null)))
        val normalSamPartition = bundle.samRecordPartitionMap.get(normalSamKeyValue).getOrElse(
          SamRecordPartition.empty(partitionId, refPartition.contigId, SamHeaderInfo.sortedHeader(refContigInfoValue, null)))
        MutectAdapter.callVariants(
          bundle.refContigInfo, tumorSamPartition, normalSamPartition, bundle.fastaPartition, rodList, intervalsValue)
          .filter(vcf => vcf.position >= refPartition.originStart && vcf.position <= refPartition.originEnd)

      } catch {
        case e: Exception => {
          Dumper.dumpBundle(bundle, Dumper.defaultString)
          throw e
        }
      }
    })

  }
}
