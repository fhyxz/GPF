package org.ncic.bioinfo.sparkseq.data.partition

import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo

/**
  * Author: wbc
  */
object SamRecordPartition {
  def empty(partitionId: Int, contigId: Int, samHeaderInfo: SamHeaderInfo): SamRecordPartition =
    new SamRecordPartition(partitionId, contigId, List[BasicSamRecord](), samHeaderInfo)
}

class SamRecordPartition(partitionId: Int, val contigId: Int,
                         val records: Iterable[BasicSamRecord],
                         val samHeaderInfo: SamHeaderInfo) extends Partition(partitionId) {
}
