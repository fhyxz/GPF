package org.ncic.bioinfo.sparkseq.processes.variantcalling

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.bundle.{RefPartitionInfoBundle, SAMBundle, VCFBundle}
import org.ncic.bioinfo.sparkseq.data.common.RefPartitionInfo
import org.ncic.bioinfo.sparkseq.data.partition.BundlePartition
import org.ncic.bioinfo.sparkseq.engine.PartitionConsumer
import org.ncic.bioinfo.sparkseq.exceptions.ResourceNotSetException
import org.ncic.bioinfo.sparkseq.processes.PartitionProcess
import org.ncic.bioinfo.sparkseq.resource.Resource

import scala.collection.mutable.ListBuffer

/**
  * Author: wbc
  */
abstract class VariantCallingProcess(name: String,
                                     referencePath: String,
                                     rodMap: Map[String, String],
                                     refPartitionInfoBundle: RefPartitionInfoBundle,
                                     inputSamBundleMap: Map[String, SAMBundle],
                                     outputVCFBundle: VCFBundle)
  extends PartitionProcess(name, referencePath, rodMap, refPartitionInfoBundle, inputSamBundleMap)
    with PartitionConsumer {

  override def getInputResourceList(): List[Resource] = {
    val inputList = ListBuffer[Resource]()
    inputList ++= inputSamBundleMap.values
    inputList.append(refPartitionInfoBundle)
    inputList.toList
  }

  override def getOutputResourceList(): List[Resource] = List(outputVCFBundle)

  override def consumeBundlePartition(bundleRDD: RDD[BundlePartition]): Unit = {
    val vcfRDDs = getVCFRecords(bundleRDD)

    // 写入结果
    outputVCFBundle.vcfRecordRDD = vcfRDDs
    outputVCFBundle.setFlag = true
  }

  def getVCFRecords(bundleRDD: RDD[BundlePartition]): RDD[VcfRecord]
}
