package org.ncic.bioinfo.sparkseq.partitioner

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.FastqRecord
import org.ncic.bioinfo.sparkseq.data.partition.FastqRecordPartition

/**
  * Author: wbc
  */
object FastqPartitioner {

  def partition(recordRdd: RDD[FastqRecord]): RDD[(Int, FastqRecordPartition)] = {
    recordRdd.mapPartitionsWithIndex((partitionId, recordIterator) => {
        Seq((partitionId, new FastqRecordPartition(partitionId, recordIterator.toIterable))).iterator
      })
  }

  def partition(recordRdd: RDD[FastqRecord],
                partitionCount: Int): RDD[(Int, FastqRecordPartition)] = {
    recordRdd.repartition(partitionCount)
      .mapPartitionsWithIndex((partitionId, recordIterator) => {
        Seq((partitionId, new FastqRecordPartition(partitionId, recordIterator.toIterable))).iterator
      })
  }
}
