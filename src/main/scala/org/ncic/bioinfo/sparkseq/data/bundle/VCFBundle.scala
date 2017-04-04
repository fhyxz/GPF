package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord
import org.ncic.bioinfo.sparkseq.data.common.VcfHeaderInfo
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object VCFBundle {

  /**
    * 未赋值的bundle
    *
    * @param key
    * @param rodName
    * @return
    */
  def undefined(key: String, vcfHeaderInfo: VcfHeaderInfo): VCFBundle = {
    val bundle = new VCFBundle(key, vcfHeaderInfo, null);
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
  def defined(key: String, vcfHeaderInfo: VcfHeaderInfo, vcfRcordRDD: RDD[VcfRecord]): VCFBundle = {
    val bundle = new VCFBundle(key, vcfHeaderInfo, vcfRcordRDD);
    bundle.setFlag = true
    bundle
  }

}

class VCFBundle(key: String,
                val vcfHeaderInfo: VcfHeaderInfo,
                var vcfRecordRDD: RDD[VcfRecord]) extends AbstractResource(key) {

}