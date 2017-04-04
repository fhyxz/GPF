package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object RODBundle {

  /**
    * 未赋值的bundle
    *
    * @param key
    * @param rodName
    * @return
    */
  def undefined(key: String, rodName: String): RODBundle = {
    val bundle = new RODBundle(key, rodName, null);
    bundle.setFlag = false
    bundle
  }

  /**
    * 赋值的bundle
    *
    * @param key
    * @param rodName
    * @param vcfRcordRDD
    * @return
    */
  def defined(key: String, rodName: String, vcfRcordRDD: RDD[VcfRecord]): RODBundle = {
    val bundle = new RODBundle(key, rodName, vcfRcordRDD);
    bundle.setFlag = true
    bundle
  }

}

class RODBundle(key: String,
                val rodName: String,
                var vcfRecordRDD: RDD[VcfRecord]) extends AbstractResource(key) {

}
