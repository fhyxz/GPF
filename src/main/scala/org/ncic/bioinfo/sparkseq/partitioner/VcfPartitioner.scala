package org.ncic.bioinfo.sparkseq.partitioner

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.common.{RefPartitionInfo, VcfHeaderInfo}
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition
import org.apache.spark.SparkContext._
import org.ncic.bioinfo.sparkseq.const.SamRecordConst

/**
  * Author: wbc
  */
object VcfPartitioner {

  /**
    * 没有overlap
    *
    * @param sc
    * @param refPartitionInfo
    * @param samRdd
    * @return
    */
  def partition(sc: SparkContext, refPartitionInfo: RefPartitionInfo, key: String,
                vcfHeader: VcfHeaderInfo, vcfRdd: RDD[VcfRecord]): RDD[VcfRecordPartition] = {
    val refPartitionInfoValue = sc.broadcast(refPartitionInfo).value
    val vcfHeaderBD = sc.broadcast(vcfHeader).value
    vcfRdd.filter(record =>
      refPartitionInfoValue.getPartitionId(record.contigId, record.position) != SamRecordConst.FAKE_PARTITION_ID)
      .groupBy(record =>
      refPartitionInfoValue.getPartitionId(record.contigId, record.position))
      .map(pair => {
        val partitionId: Int = pair._1
        val recordIter: Iterable[VcfRecord] = pair._2
        val contigId = refPartitionInfoValue.getPartitionRange(partitionId)._3
        new VcfRecordPartition(partitionId, key, contigId, vcfHeaderBD, recordIter)
      })
  }

  /**
    * 带overlap
    *
    * @param refPartitionInfo
    * @param samRdd
    * @return
    */
  def partition(sc: SparkContext, refPartitionInfo: RefPartitionInfo, key: String,
                vcfHeader: VcfHeaderInfo, vcfRdd: RDD[VcfRecord], overlapLen: Int): RDD[(Int, VcfRecordPartition)] = {
    val refPartitionInfoValue = sc.broadcast(refPartitionInfo).value
    val vcfHeaderBD = sc.broadcast(vcfHeader).value
    vcfRdd.filter(record =>
      refPartitionInfoValue.getPartitionId(record.contigId, record.position) != SamRecordConst.FAKE_PARTITION_ID)
      .groupBy(record =>
        refPartitionInfoValue.getPartitionId(record.contigId, record.position))
      .flatMap(pair => {
        val partitionId = pair._1
        val recordIter = pair._2.toList
        val partitionRange = refPartitionInfoValue.getPartitionRange(partitionId)
        val beforeOverlap: List[(Int, VcfRecord)] = {
          if (partitionId > 0) {
            //需要加前overlap
            val idx = partitionRange._1 + overlapLen
            recordIter.filter(record => record.position < idx).map(record => (partitionId - 1, record))
          }
          else {
            List[(Int, VcfRecord)]()
          }
        }
        val afterOverlap: List[(Int, VcfRecord)] = {
          if (refPartitionInfoValue.getPartitionRange(partitionId + 1)._1 != -1) {
            //需要加后overlap
            val idx = partitionRange._2 - overlapLen
            recordIter.filter(record => record.position > idx).map(record => (partitionId + 1, record))
          }
          else {
            List[(Int, VcfRecord)]()
          }
        }

        val middle: List[(Int, VcfRecord)] = recordIter.map(record => (partitionId, record))
        beforeOverlap ++ middle ++ afterOverlap
      })
      .groupByKey()
      .map(pair => {
        val partitionId = pair._1
        val contigId = refPartitionInfoValue.getPartitionRange(pair._1)._3
        (partitionId, new VcfRecordPartition(partitionId, key, contigId, vcfHeaderBD, pair._2))
      })
  }
}
