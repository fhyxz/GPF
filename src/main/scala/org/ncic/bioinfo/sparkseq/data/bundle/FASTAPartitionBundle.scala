package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo
import org.ncic.bioinfo.sparkseq.data.partition.FastaPartition
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object FASTAPartitionBundle {
  /**
    * 未实现内容
    *
    * @param key
    * @param refContigInfo
    * @return
    */
  def undefined(key: String, refContigInfo: RefContigInfo): FASTAPartitionBundle = {
    val bundle = new FASTAPartitionBundle(key, refContigInfo, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 已实现内容
    *
    * @param key
    * @param refContigInfo
    * @return
    */
  def defined(key: String, refContigInfo: RefContigInfo,
            fastaPartitionRDD: RDD[(Int, FastaPartition)]): FASTAPartitionBundle = {
    val bundle = new FASTAPartitionBundle(key, refContigInfo, fastaPartitionRDD)
    bundle.setFlag = true
    bundle
  }
}

class FASTAPartitionBundle(key: String,
                           val refContigInfo: RefContigInfo,
                           var fastaPartitionRDD: RDD[(Int, FastaPartition)]) extends AbstractResource(key) {

}
