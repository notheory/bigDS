package org.apache.bigds.mining.association.PFP

import org.apache.spark.{SparkContext, SparkConf}

import scala.compat.Platform._

/**
 * Created by clin3 on 2014/12/24.
 */
object DistFPGrowthTest {
  def main(args: Array[String]): Unit = {
    val supportThreshold = args(0).toDouble
    val fileName = args(1)

    //Initialize SparkConf.
    val conf = new SparkConf()
    conf.setMaster("spark://sr471:7177").setAppName("FPGrowth").set("spark.cores.max", "256").set("spark.executor.memory", "160G")

    //Initialize SparkContext.
    val sc = new SparkContext(conf)

    //Create distributed datasets from hdfs.
//    val input = sc.textFile("hdfs://sr471:54311/user/clin/fpgrowth/input/" + fileName, DistFPGrowth.DEFAULT_NUM_GROUPS)
    val input = sc.textFile("hdfs://sr471:54311/user/clin/fpgrowth/input/" + fileName)

    val startTime = currentTime
    val rdd = DistFPGrowth.run(input, supportThreshold)
    val count = rdd.count()
    
    val endTime = currentTime
    val totalTime: Double = endTime - startTime

    println("---------------------------------------------------------")
    println("This program totally took " + totalTime/1000 + " seconds.")

    println("---------------------------------------------------------")
    println("Number of frequent itemsets = " + count)

    println("---------------------------------------------------------")
    println("Frequent Itemsets")
    rdd.collect.foreach { case (itemsets, cnt) =>
      println("<" + itemsets + ", " + cnt + ">")
    }

    //Stop SparkContext.
    sc.stop()
  }
}
