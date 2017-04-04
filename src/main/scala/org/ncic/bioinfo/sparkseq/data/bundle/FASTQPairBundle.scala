package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.FastqPairRecord
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object FASTQPairBundle {
  /**
    * 未赋值的bundle
    *
    * @param key
    * @return
    */
  def undefined(key: String): FASTQPairBundle = {
    val bundle = new FASTQPairBundle(key, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 赋值的bundle
    *
    * @param key
    * @param fastqPairRecordRDD
    * @return
    */
  def defined(key: String,
              fastqPairRecordRDD: RDD[FastqPairRecord]): FASTQPairBundle = {
    val bundle = new FASTQPairBundle(key, fastqPairRecordRDD)
    bundle.setFlag = true
    bundle
  }
}

class FASTQPairBundle(key: String, var fastqPairRecordRDD: RDD[FastqPairRecord])
  extends AbstractResource(key) {

}
