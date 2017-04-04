package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.{FastqPairRecord, FastqRecord}
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo
import org.ncic.bioinfo.sparkseq.data.partition.FastaPartition
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object FASTQBundle {

  /**
    * 未赋值的bundle
    *
    * @param key
    * @return
    */
  def undefined(key: String): FASTQBundle = {
    val bundle = new FASTQBundle(key, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 已赋值的bundle
    *
    * @param key
    * @param fastqRecordRDD
    * @return
    */
  def defined(key: String,
              fastqRecordRDD: RDD[FastqRecord]): FASTQBundle = {
    val bundle = new FASTQBundle(key, fastqRecordRDD)
    bundle.setFlag = true
    bundle
  }
}

class FASTQBundle(key: String, var fastqRecordRDD: RDD[FastqRecord])
  extends AbstractResource(key) {

}
