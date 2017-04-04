package org.ncic.bioinfo.sparkseq.fileio

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.{BasicSamRecord, FastqPairRecord, FastqRecord, VcfRecord}
import org.ncic.bioinfo.sparkseq.data.common.{SamHeaderInfo, VcfHeaderInfo}

/**
  * Author: wbc
  */
trait FileWriter {

  def writeFastq(records: Iterable[FastqRecord], filePath: String)

  def writeFastqRdd(sc: SparkContext, rdd: RDD[FastqRecord], filePath: String)

  def writeFastqPair(records: Iterable[FastqPairRecord], filePath1: String, filePath2: String)

  def writeFastqPairRdd(sc: SparkContext, rdd: RDD[FastqPairRecord], filePath1: String, filePath2: String)

  def writeSam(headerInfo: SamHeaderInfo, records: Iterable[BasicSamRecord], filePath: String)

  def writeSamRdd(sc: SparkContext, headerInfo: SamHeaderInfo, rdd: RDD[BasicSamRecord], filePath: String)

  def writeVcf(headerInfo: VcfHeaderInfo, records: Iterable[VcfRecord], filePath: String)

  def writeVcfRdd(sc: SparkContext, headerInfo: VcfHeaderInfo, rdd: RDD[VcfRecord], filePath: String)
}
