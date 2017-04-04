package org.ncic.bioinfo.sparkseq.fileio

import java.io.{BufferedReader, InputStreamReader}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileSystem, Path}

/**
  * @author wbc
  */
object HDFSReader {
  def apply(path: String): HDFSReader = {
    val hdfs: FileSystem = FileSystem.get(new Configuration)
    val sourcePath = new Path(path)

    val inputStream: FSDataInputStream = hdfs.open(sourcePath)
    val bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
    new HDFSReader(bufferedReader)
  }
}

class HDFSReader(inputStream: BufferedReader) {

  def readLine(): String = {
    inputStream.readLine()
  }

}
