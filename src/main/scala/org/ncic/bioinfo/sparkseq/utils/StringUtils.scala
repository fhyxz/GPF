package org.ncic.bioinfo.sparkseq.utils

/**
  * Author: wbc
  */
object StringUtils {

  def EMPTY = ""

  def isNotEmpty(str: String): Boolean = {
    str != null && str.length > 0
  }

  def isEmpty(str: String): Boolean = {
    str == null || str.length == 0
  }

  def split(str: String, seperator: String): java.util.Iterator[String] = {
    com.google.common.base.Splitter.on(seperator).split(str).iterator()
  }

  def join(strings: List[String], seperator: Char): String = {
    org.apache.commons.lang3.StringUtils.join(strings, seperator)
  }

}
