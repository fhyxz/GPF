package org.ncic.bioinfo.sparkseq.data.partition

import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo

/**
  * Author: wbc
  */
class BundlePartition(partitionId: Int,
                      val refContigInfo: RefContigInfo,
                      val fastaPartition: FastaPartition,
                      val samRecordPartitionMap: Map[String, SamRecordPartition],
                      val rodPartitionMap: Map[String, VcfRecordPartition])
  extends Partition(partitionId) {

}
