package org.ncic.bioinfo.sparkseq.data.bundle

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition
import org.ncic.bioinfo.sparkseq.resource.AbstractResource

/**
  * Author: wbc
  */
class VCFPartitionBundle(key: String, var vcfPartitionPairRDD: RDD[(Int, VcfRecordPartition)])
  extends AbstractResource(key) {

}
