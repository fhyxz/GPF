package org.ncic.bioinfo.sparkseq.data.common

import org.ncic.bioinfo.sparkseq.utils.StringUtils

/**
  * Author: wbc
  */
object Locus {
  def apply(line: String, refContigInfo: RefContigInfo): Locus = {
    val iter = StringUtils.split(line, "\t")
    val contigName = iter.next()
    val contigId = refContigInfo.getId(contigName)
    val start = iter.next().toInt
    val stop = iter.next().toInt
    /*val pos1 = line.indexOf(':')
    val contigName = line.substring(0, pos1)
    val contigId = refContigInfo.getId(contigName)
    val pos2 = line.indexOf('-')
    val start = line.substring(pos1 + 1, pos2).toInt
    val stop = line.substring(pos2 + 1).toInt*/
    new Locus(contigId, contigName, start, stop)
  }
}

class Locus(val contigId: Int, val contigName: String, val start: Int, val stop: Int) extends Serializable {

}
