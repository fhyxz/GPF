package org.ncic.bioinfo.sparkseq.processes.cleaning

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.bundle.{RefPartitionInfoBundle, SAMBundle}
import org.ncic.bioinfo.sparkseq.data.common.{RefPartitionInfo, SamHeaderInfo}
import org.ncic.bioinfo.sparkseq.data.partition.{BundlePartition, SamRecordPartition}
import org.ncic.bioinfo.sparkseq.engine.{PartitionConsumer, PartitionGenerator}
import org.ncic.bioinfo.sparkseq.processes.PartitionProcess
import org.ncic.bioinfo.sparkseq.resource.Resource

import scala.collection.mutable.ListBuffer

/**
  * Author: wbc
  */
abstract class DataCleanProcess(name: String,
                                referencePath: String,
                                rodMap: Map[String, String],
                                refPartitionInfoBundle: RefPartitionInfoBundle,
                                inputSamBundleMap: Map[String, SAMBundle],
                                outputSamBundleMap: Map[String, SAMBundle])
  extends PartitionProcess(name, referencePath, rodMap, refPartitionInfoBundle, inputSamBundleMap)
    with PartitionGenerator {

  override def getInputResourceList(): List[Resource] = {
    val inputList = ListBuffer[Resource]()
    inputList ++= inputSamBundleMap.values
    inputList.append(refPartitionInfoBundle)
    inputList.toList
  }

  override def getOutputResourceList(): List[Resource] = outputSamBundleMap.values.toList

  override def generateBundlePartition(bundleRDD: RDD[BundlePartition]): RDD[BundlePartition] = {
    getCleanedBundleRDD(bundleRDD)
  }

  override def consumeBundlePartition(bundleRDD: RDD[BundlePartition]): Unit = {
    val resultBundleRDD = getCleanedBundleRDD(bundleRDD)
    val refContigInfoValue = sc.broadcast(refPartitionInfoBundle.refPartitionInfo.getRefContigInfo).value

    // 写入结果
    val samMapRDD = resultBundleRDD.map(bundle => bundle.samRecordPartitionMap)
    outputSamBundleMap.foreach(samBundleWithKey => {
      val key = samBundleWithKey._1
      val samBundle = samBundleWithKey._2
      val keyBD = sc.broadcast(key).value
      val samRDD = resultBundleRDD.flatMap(bundle => {
        val refPartition = bundle.fastaPartition
        val samPartition = bundle.samRecordPartitionMap.get(keyBD)
        samPartition.getOrElse(SamRecordPartition.empty(refPartition.partitionId,  refPartition.contigId, SamHeaderInfo.sortedHeader(refContigInfoValue, null)))
          .records.filter(record =>
          (record.position >= refPartition.originStart && record.position <= refPartition.originEnd))
      })
      samBundle.setFlag = true
      samBundle.samRecordRDD = samRDD
    })
  }

  def getCleanedBundleRDD(bundleRDD: RDD[BundlePartition]): RDD[BundlePartition]
}
