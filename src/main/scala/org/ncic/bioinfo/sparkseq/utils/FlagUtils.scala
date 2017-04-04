package org.ncic.bioinfo.sparkseq.utils

import org.ncic.bioinfo.sparkseq.data.common.Flags


/**
  * Author: wbc
  */
object FlagUtils {

  val MULTI_SEGMENT = 1
  val EACH_SEGMENT_ALIGNED = 2
  val SEGMENT_UNMAPPED = 4
  val NEXT_SEGMENT_UNMAPPED = 8
  val SEQUENCE_REVERSED = 16
  val NEXT_SEQUENCE_REVERSED = 32
  val FIRST_SEGMENT_IN_MAPPED = 64
  val LAST_SEGMENT_IN_MAPPED = 128
  val SECONDARY_ALIGNMENT = 256
  val NO_PASSING_FILTERS = 512
  val PCR_DUPLICATE = 1024
  val SUPPLEMENTARY_ALIGNMENT = 2048

  def initialFlag = new Flags
}