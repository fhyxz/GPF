package org.ncic.bioinfo.sparkseq.partitioner

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord
import org.ncic.bioinfo.sparkseq.data.common.{RefPartitionInfo, SamHeaderInfo}
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition
import org.apache.spark.SparkContext._
import org.ncic.bioinfo.sparkseq.const.SamRecordConst

/**
  * Author: wbc
  */
object SamRecordPartitioner extends Serializable {

  /**
    * 没有overlap
    *
    * @param refPartitionInfo
    * @param samRdd
    * @return
    */
  def partition(sc: SparkContext, refPartitionInfo: RefPartitionInfo, samHeaderInfo: SamHeaderInfo,
                samRdd: RDD[BasicSamRecord]): RDD[(Int, SamRecordPartition)] = {
    val refPartitionInfoValue = sc.broadcast(refPartitionInfo).value
    val samHeaderInfoValue = sc.broadcast(samHeaderInfo).value
    samRdd.filter(record =>
      refPartitionInfoValue.getPartitionId(record.contigId, record.position) != SamRecordConst.FAKE_PARTITION_ID)
      .groupBy(record =>
        refPartitionInfoValue.getPartitionId(record.contigId, record.position))
      .map(pair => {
        val partitionId: Int = pair._1
        val recordIter: Iterable[BasicSamRecord] = pair._2
        val contigId = refPartitionInfoValue.getPartitionRange(partitionId)._3
        (partitionId, new SamRecordPartition(partitionId, contigId, recordIter, samHeaderInfoValue))
      })
  }

  /**
    * 带overlap
    *
    * @param refPartitionInfo
    * @param samRdd
    * @return
    */
  def partition(sc: SparkContext, refPartitionInfo: RefPartitionInfo, samHeaderInfo: SamHeaderInfo,
                samRdd: RDD[BasicSamRecord], overlapLen: Int): RDD[(Int, SamRecordPartition)] = {
    val refPartitionInfoValue = sc.broadcast(refPartitionInfo).value
    val samHeaderInfoValue = sc.broadcast(samHeaderInfo).value
    samRdd.flatMap(record => {
      val partitionId = refPartitionInfoValue.getPartitionId(record.contigId, record.position)
      if (partitionId == SamRecordConst.FAKE_PARTITION_ID) {
        List()
      } else {
        val partitionRange = refPartitionInfoValue.getPartitionRange(partitionId)
        val inBeforeIdx = partitionRange._1 + overlapLen
        val inAfterIdx = partitionRange._2 - overlapLen
        if (partitionId > 0 && record.position <= inBeforeIdx) {
          List((partitionId - 1, record), (partitionId, record))
        } else if (refPartitionInfoValue.getPartitionRange(partitionId + 1)._1 != -1 && record.position >= inAfterIdx) {
          List((partitionId, record), (partitionId + 1, record))
        } else {
          List((partitionId, record))
        }
      }
    })
      .groupByKey()
      .map(pair => {
        val partitionId = pair._1
        val contigId = refPartitionInfoValue.getPartitionRange(partitionId)._3
        (partitionId, new SamRecordPartition(partitionId, contigId, pair._2, samHeaderInfoValue))
      })
  }
}
