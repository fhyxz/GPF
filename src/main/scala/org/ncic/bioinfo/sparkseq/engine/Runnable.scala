package org.ncic.bioinfo.sparkseq.engine

/**
  * Author: wbc
  */
trait Runnable {

  /**
    * 标识是否该过程是否已经完成。
    */
  var done: Boolean = false

  /**
    * 执行过程。
    * 建议在执行后将done置为true。
    */
  def run()

}
