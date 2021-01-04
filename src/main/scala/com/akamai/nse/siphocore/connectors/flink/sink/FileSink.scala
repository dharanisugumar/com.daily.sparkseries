package com.akamai.nse.siphocore.connectors.flink.sink

import com.akamai.nse.siphocore.connectors.{BatchSinkConnector, BatchSourceConnector}
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

class FileSink (fileName: String, fileDelim:String = ",") extends BatchSinkConnector[Row] {
  private lazy val log = LoggerFactory.getLogger(this.getClass)
  @BeanProperty var mode: WriteMode = WriteMode.OVERWRITE
  @BeanProperty var runPostProcess: Boolean = false
  @BeanProperty var completedDir: String = "file:///tmp"
  val INTERVAL = 1000

  override def loader(dataSet: DataSet[Row]) = {
    dataSet.rebalance().writeAsText(fileName, mode)
  }
}