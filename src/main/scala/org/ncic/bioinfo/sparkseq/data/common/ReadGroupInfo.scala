package org.ncic.bioinfo.sparkseq.data.common

/**
  * Author: wbc
  */
object ReadGroupInfo extends Serializable {
  val defaultSampleName = "newSample"
  val defaultPlatform = "illumina"
  val defaultLib = "lib1"
  val defaultPlatformUnit = "unit1"

  def apply(id: String): ReadGroupInfo = {
    new ReadGroupInfo(id, defaultSampleName, defaultPlatform, defaultLib, defaultPlatformUnit)
  }

  def apply(id: String, sampleName: String): ReadGroupInfo = {
    new ReadGroupInfo(id, sampleName, defaultPlatform, defaultLib, defaultPlatformUnit)
  }
}

class ReadGroupInfo(val id: String,
                    val sample: String,
                    val platform: String,
                    val lib: String,
                    val platformUnit: String) extends Serializable {

}
