package org.ncic.bioinfo.sparkseq.data.partition

import org.ncic.bioinfo.sparkseq.data.basic.FastqRecord

/**
  * Author: wbc
  */
class FastqRecordPartition(partitionId: Int) extends Partition(partitionId) {

  var records: Iterable[FastqRecord] = null

  def this(partitionId: Int, records: Iterable[FastqRecord]) {
    this(partitionId)
    this.records = records
  }
}
