package org.ncic.bioinfo.sparkseq.resource

/**
  * Author: wbc
  */
abstract class AbstractResource(resKey: String) extends Resource {

  val key: String = resKey
  // default null
  var setFlag: Boolean = false // default false

  override def isSet: Boolean = {
    setFlag
  }
}
