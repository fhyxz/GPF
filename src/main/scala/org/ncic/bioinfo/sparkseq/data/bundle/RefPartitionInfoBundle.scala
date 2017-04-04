package org.ncic.bioinfo.sparkseq.data.bundle

import org.ncic.bioinfo.sparkseq.data.common.RefPartitionInfo
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * @author wbc
  */
object RefPartitionInfoBundle {
  /**
    * 未实现内容
    *
    * @param key
    * @return
    */
  def undefined(key: String): RefPartitionInfoBundle = {
    val bundle = new RefPartitionInfoBundle(key, null)
    bundle.setFlag = false
    bundle
  }

  /**
    * 已实现内容
    *
    * @param key
    * @param refPartitionInfo
    * @return
    */
  def defined(key: String, refPartitionInfo: RefPartitionInfo): RefPartitionInfoBundle = {
    val bundle = new RefPartitionInfoBundle(key, refPartitionInfo)
    bundle.setFlag = true
    bundle
  }
}

class RefPartitionInfoBundle(key: String,
                             var refPartitionInfo: RefPartitionInfo) extends AbstractResource(key) {
}
