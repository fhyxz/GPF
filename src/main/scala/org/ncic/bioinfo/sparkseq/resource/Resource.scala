package org.ncic.bioinfo.sparkseq.resource

/**
  * Author: wbc
  */
trait Resource {

  val key: String

  def isSet: Boolean
}
