package org.ncic.bioinfo.sparkseq.utils

/**
  * Author: wbc
  */
import java.io.{File, FileInputStream}
import java.util.Properties

import org.apache.commons.io.FilenameUtils
import org.ncic.bioinfo.sparkseq.const.BinTools

object FileUtils {

  def isExists(path: String): Boolean = {
    new File(path).exists()
  }

  def getDirPathNoEndSeparator(path: String): String = {
    FilenameUtils.getFullPathNoEndSeparator(path)
  }

  def join(basePath: String, fileName: String) = {
    FilenameUtils.concat(basePath, fileName)
  }
}