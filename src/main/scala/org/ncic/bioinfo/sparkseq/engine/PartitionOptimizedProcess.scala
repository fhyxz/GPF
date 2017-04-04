package org.ncic.bioinfo.sparkseq.engine

import org.apache.spark.rdd.RDD
import org.ncic.bioinfo.sparkseq.data.partition.BundlePartition
import org.ncic.bioinfo.sparkseq.exceptions.PipelineException

/**
  * Author: wbc
  */
abstract class PartitionOptimizedProcess(name: String) extends AbstractProcess(name) {


  def runProcess(): Unit = {

    var inputBundle = if (this.isInstanceOf[PartitionConsumer]
      && this.asInstanceOf[PartitionConsumer].setAsChainConsumer) {
      this.asInstanceOf[PartitionConsumer].parentProcess.resultBundleRDD
    } else {
      getBundlePartition()
    }

    if (this.isInstanceOf[PartitionGenerator]) {
      //如果是generator，则根据是否设置为generator决定要把partition存起来还是消费掉
      val generator = this.asInstanceOf[PartitionGenerator]
      if (generator.setAsChainGenerator) {
        generator.resultBundleRDD = generator.generateBundlePartition(inputBundle)
      } else {
        generator.consumeBundlePartition(inputBundle)
      }
    } else if (this.isInstanceOf[PartitionConsumer]) {
      //如果是纯的consumer，就直接消费掉就好了
      val consumer = this.asInstanceOf[PartitionConsumer]
      consumer.consumeBundlePartition(inputBundle)
    }

  }

  protected def getBundlePartition(): RDD[BundlePartition]

  override def run(): Unit = {
    if (resourcePool == null) {
      throw new PipelineException("Resource pool is not set yet <process: " + name + ">")
    }

    // 运行process
    runProcess()

    done = true
  }
}
