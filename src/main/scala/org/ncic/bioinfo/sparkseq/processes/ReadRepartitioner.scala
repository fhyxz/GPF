package org.ncic.bioinfo.sparkseq.processes

import org.ncic.bioinfo.sparkseq.data.bundle.{RefPartitionInfoBundle, SAMBundle}
import org.ncic.bioinfo.sparkseq.data.common.RefPartitionInfo
import org.ncic.bioinfo.sparkseq.engine.AbstractProcess
import org.ncic.bioinfo.sparkseq.exceptions.{ResourceNotSetException, ResourceSetException}
import org.ncic.bioinfo.sparkseq.resource.Resource
import org.apache.spark.SparkContext._
import org.apache.spark.storage.StorageLevel
import org.ncic.bioinfo.sparkseq.const.BinTools

/**
  * @author wbc
  */
object ReadRepartitioner {
  def apply(name: String,
            inputSamBundle: SAMBundle,
            outputRefPartitionInfo: RefPartitionInfoBundle,
            refPartitionInfo: RefPartitionInfo): ReadRepartitioner =
    new ReadRepartitioner(name, inputSamBundle, outputRefPartitionInfo, refPartitionInfo)
}

class ReadRepartitioner(name: String,
                        inputSamBundle: SAMBundle,
                        outputRefPartitionInfo: RefPartitionInfoBundle,
                        refPartitionInfo: RefPartitionInfo) extends AbstractProcess(name) {

  override def getInputResourceList(): List[Resource] = List(inputSamBundle)

  override def getOutputResourceList(): List[Resource] = List(outputRefPartitionInfo)

  override def runProcess(): Unit = {
    if (inputSamBundle != null && !inputSamBundle.isSet) {
      throw new ResourceNotSetException(inputSamBundle.key)
    }
    if (outputRefPartitionInfo.isSet) {
      throw new ResourceSetException(outputRefPartitionInfo.key)
    }

    val samRDD = inputSamBundle.samRecordRDD
    samRDD.persist(StorageLevel.DISK_ONLY)
    val refPartitionInfoValue = sc.broadcast(refPartitionInfo).value
    val readStatistic = samRDD.map(record =>
      (refPartitionInfoValue.getPartitionId(record.contigId, record.position), 1))
      .reduceByKey(_ + _)
      .collect()

    val averageLength = readStatistic.map(_._2).sum / readStatistic.length
    val skipPartitions = readStatistic.filter(pair => pair._2 > BinTools.splitPartitionThres * averageLength)
      .map(_._1).toSet

    println("*****Skip partition count:" + skipPartitions.size)
    outputRefPartitionInfo.refPartitionInfo = RefPartitionInfo(refPartitionInfo, skipPartitions)
    outputRefPartitionInfo.setFlag = true
  }

}
