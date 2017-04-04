package org.ncic.bioinfo.sparkseq.tools

import org.apache.log4j.Logger

/**
  * 使用log4j，封装接口使得未来可以替换log工具
  *
  * Author: wbc
  */
object LOG {
  def apply(className: String): LOG = new LOG(className)
}

class LOG(className: String) {

  private val logger: Logger = Logger.getLogger(className)

  def DEBUG(content: String): Unit = {
    logger.debug(content)
  }

  def INFO(content: String): Unit = {
    logger.info(content)
  }

  def WARN(content: String): Unit = {
    logger.warn(content)
  }

  def ERROR(content: String): Unit = {
    logger.error(content)
  }

}
