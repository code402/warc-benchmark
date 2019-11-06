package com.cldellow.warc

import com.cldellow.warc.framework._
import com.cldellow.warc.io._
import com.cldellow.warc.cli.commands.job._
import com.codahale.metrics.MetricRegistry

object App {
  private val metrics = new MetricRegistry

  def main(args: Array[String]): Unit = {
    S3.FileUrlsOk = true
    S3.HttpUrlsOk = true

    val config = new ConfigWithWarcOptions[GrepConfig](
      "zst",
      List(".*\\.com/.*").toArray,
      Nil.toArray,
      false,
      Nil.toArray,
      GrepConfig(
        100,
        0,
        """(youtu\.be/|youtube\.com/(watch\?(.*&)?v=|(embed|v)/))([^?&"'>]+)"""
      )
    )

    var hits = 0
    var entries = 0
    var matchingEntries = 0

    val handler = new GrepHandler(None, "bucket", "userid", "jobid", config, metrics) {
      override def onWarcStartTest(warc: Warc): Unit = {
        entries += 1
      }

      override def recordUrl(warc: Warc, grepResults: Array[String]): Unit = {
        matchingEntries += 1
        hits += grepResults.length
      }

      override def onFinish(bucket: String, key: String, bytes: Array[Byte], size: Int): Unit = {
        println(s"code402: ${hits} of ${matchingEntries}/${entries}")
      }
    }

    var filename = "http://commoncrawl.s3.amazonaws.com/crawl-data/CC-MAIN-2019-30/segments/1563195523840.34/warc/CC-MAIN-20190715175205-20190715200159-00000.warc.gz"
    args.headOption match {
      case Some(x) if x.startsWith("http://") || x.startsWith("https://") =>
        filename = x
      case Some(x) =>
        filename = "file://" + x
      case None =>
    }

    val input = Input(
      Source.fromName("warc"),
      List(filename).toArray,
      0
    )
    handler.handle(input, () => {})
  }
}
