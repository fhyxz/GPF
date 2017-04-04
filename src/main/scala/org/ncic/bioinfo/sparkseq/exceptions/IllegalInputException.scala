package org.ncic.bioinfo.sparkseq.exceptions

/**
  * Author: wbc
  */
object IllegalInputException {
  def apply(msg: String): IllegalInputException = new IllegalInputException(msg)

  def apply(): IllegalInputException = new IllegalInputException("")
}

class IllegalInputException(msg: String)
  extends RuntimeException(msg) {

}
