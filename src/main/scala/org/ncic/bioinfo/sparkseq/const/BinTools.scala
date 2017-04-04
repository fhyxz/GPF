package org.ncic.bioinfo.sparkseq.const

import java.io.FileInputStream
import java.util.Properties

import org.ncic.bioinfo.sparkseq.utils.FileUtils

/**
  * Author: wbc
  */
object BinTools {
  val binDirPath = FileUtils.join(FileUtils.getDirPathNoEndSeparator(
    FileUtils.getDirPathNoEndSeparator(
      FileUtils.getDirPathNoEndSeparator(
        FileUtils.getDirPathNoEndSeparator(
          FileUtils.getDirPathNoEndSeparator(
            FileUtils.getDirPathNoEndSeparator(
              FileUtils.getDirPathNoEndSeparator(
                FileUtils.getDirPathNoEndSeparator(
                  this.getClass().getResource("").getPath())))))))), "bin")

  val bwaPath = {
    val tmpPath = FileUtils.join(binDirPath, "bwa")
    if (tmpPath.startsWith("file:")) tmpPath.substring(5) else tmpPath
  }

  val bwaLibPath = {
    val tmpPath = FileUtils.join(binDirPath, "libbwajni.so")
    if (tmpPath.startsWith("file:")) tmpPath.substring(5) else tmpPath
  }

  val confPath = {
    val tmpPath = FileUtils.join(binDirPath, "config.properties")
    if (tmpPath.startsWith("file:")) tmpPath.substring(5) else tmpPath
  }

  val localTmpFilePath = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("localWorkDir")
  }

  val publicTmpFilePath = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("globalWorkDir")
  }

  val processOptimize = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("processOptimize").toBoolean
  }

  val shuffleCompress = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("shuffleCompress").toBoolean
  }

  val DEFAULT_PARTITION_LENGTH = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("partitonLength").toInt
  }

  val splitPartitionThres = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("splitPartitionThres").toInt
  }

  val bqsrGatherThreads = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("bqsrGatherThreads").toInt
  }

  val repartitionCount = {
    val properties = new Properties()
    properties.load(new FileInputStream(confPath))
    properties.getProperty("activeRegionRepartitionCount").toInt
  }
}
