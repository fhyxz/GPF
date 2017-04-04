package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
object SAMBundle {

  /**
    * 未赋值的bundle
    *
    * @param key
    * @param samHeaderInfo
    * @return
    */
  def undefined(key: String,
                samHeaderInfo: SamHeaderInfo): SAMBundle = {
    val bundle = new SAMBundle(key, samHeaderInfo, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 从父亲sam bundle那里继承header信息，数据部分为空，是undefined的
    *
    * @param key resource key
    * @param parentSamBundle 父亲bundle
    * @return
    */
  def undefinedFromParent(key: String,
                parentSamBundle: SAMBundle): SAMBundle = {
    val bundle = new SAMBundle(key, parentSamBundle.samHeaderInfo, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 赋值的bundle
    *
    * @param key
    * @param samHeaderInfo
    * @param samRecordRDD
    * @return
    */
  def defined(key: String,
              samHeaderInfo: SamHeaderInfo,
              samRecordRDD: RDD[BasicSamRecord]): SAMBundle = {
    val bundle = new SAMBundle(key, samHeaderInfo, samRecordRDD)
    bundle.setFlag = true
    bundle
  }
}

class SAMBundle(key: String,
                val samHeaderInfo: SamHeaderInfo,
                var samRecordRDD: RDD[BasicSamRecord]) extends AbstractResource(key) {

}
