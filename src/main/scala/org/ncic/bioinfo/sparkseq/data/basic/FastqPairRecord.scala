package org.ncic.bioinfo.sparkseq.data.basic

import java.io.Serializable

import org.ncic.bioinfo.sparkseq.exceptions.PipelineException

/**
  * 为了进行pair-end的mapping
  * Author: wbc
  */
object FastqPairRecord extends Serializable {

  def apply(fastqRecord1: FastqRecord, fastqRecord2: FastqRecord): FastqPairRecord = {
    if (fastqRecord1.compressFlag != fastqRecord2.compressFlag) {
      throw new PipelineException("Can't zip a compressed fastq with a none-compressed fastq")
    }
    new FastqPairRecord(fastqRecord1.compressFlag, fastqRecord1.descriptionLine,
      fastqRecord1.sequence, fastqRecord2.sequence,
      fastqRecord1.quality, fastqRecord2.quality)
  }
}

class FastqPairRecord(val compressFlag: Boolean,
                      val descriptionLine: String,
                      val sequence1: Array[Byte],
                      val sequence2: Array[Byte],
                      val quality1: Array[Byte],
                      val quality2: Array[Byte]) extends Serializable {

}
