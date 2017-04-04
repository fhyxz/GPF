package org.ncic.bioinfo.sparkseq.data.common

/**
  * Author: wbc
  */
class Flags extends Serializable{
  var flags = 0

  def addFlag(addFlag: Int): Flags = {
    flags += addFlag
    this
  }

  def addFlags(addFlags: Flags): Flags = {
    flags += addFlags.intValue
    this
  }

  def intValue = flags
}
