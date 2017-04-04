package org.ncic.bioinfo.sparkseq.data.partition

import org.ncic.bioinfo.sparkseq.data.basic.FastqPairRecord

/**
  * Author: wbc
  */
class FastqPairRecordPartition(partitionId: Int) extends Partition(partitionId) {

  var records: Iterable[FastqPairRecord] = null

  def this(partitionId: Int, records: Iterable[FastqPairRecord]) {
    this(partitionId)
    this.records = records
  }
}
