package org.ncic.bioinfo.sparkseq.fileio

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.basic.{BasicSamRecord, FastqPairRecord, FastqRecord, VcfRecord}
import org.ncic.bioinfo.sparkseq.data.common.{RefContigInfo, VcfHeaderInfo}

/**
  * Author: wbc
  */
trait FileLoader {

  def loadFastq(filePath: String): List[FastqRecord]

  def loadFastqToRdd(sc: SparkContext, filePath: String): RDD[FastqRecord]

  def loadFastqPair(filePath1: String, filePath2: String): List[FastqPairRecord]

  def loadFastqPairToRdd(sc: SparkContext, filePath1: String, filePath2: String): RDD[FastqPairRecord]

  def loadSam(filePath: String, refContigInfo: RefContigInfo): List[BasicSamRecord]

  def loadSamToRdd(sc: SparkContext, filePath: String, refContigInfo: RefContigInfo): RDD[BasicSamRecord]

  def loadVcf(filePath: String, refContigInfo: RefContigInfo): List[VcfRecord]

  def loadVcfToRdd(sc: SparkContext, filePath: String, refContigInfo: RefContigInfo): RDD[VcfRecord]

  def loadVcfHeader(filePath: String): VcfHeaderInfo

}
