package org.ncic.bioinfo.sparkseq.data.partition

import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.common.VcfHeaderInfo

/**
  * Author: wbc
  */
object VcfRecordPartition {
  def empty(partitionId: Int, key: String, contigId: Int, vcfHeader: VcfHeaderInfo): VcfRecordPartition =
    new VcfRecordPartition(partitionId, key, contigId, vcfHeader, List[VcfRecord]())
}

class VcfRecordPartition(partitionId: Int,
                         val key: String,
                         val contigId: Int,
                         val vcfHeader: VcfHeaderInfo,
                         val records: Iterable[VcfRecord])
  extends Partition(partitionId) {

}
