package org.ncic.bioinfo.sparkseq.partitioner

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.FastqPairRecord
import org.ncic.bioinfo.sparkseq.data.partition.FastqPairRecordPartition

/**
  * Author: wbc
  */
object FastqPairPartitioner {

  def partition(recordRdd: RDD[FastqPairRecord]): RDD[(Int, FastqPairRecordPartition)] = {
    recordRdd.mapPartitionsWithIndex((partitionId, recordIterator) => {
      Seq((partitionId, new FastqPairRecordPartition(partitionId, recordIterator.toIterable))).iterator
    })
  }

  def partition(recordRdd: RDD[FastqPairRecord],
                partitionCount: Int): RDD[(Int, FastqPairRecordPartition)] = {
    recordRdd.repartition(partitionCount)
      .mapPartitionsWithIndex((partitionId, recordIterator) => {
        Seq((partitionId, new FastqPairRecordPartition(partitionId, recordIterator.toIterable))).iterator
      })
  }

}
