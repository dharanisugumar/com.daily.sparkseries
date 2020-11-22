package com.akamai.nsecore.Connectors.Sink

import com.akamai.nsecore.Connectors.{BasicSinkConnector, BasicSourceConnector}
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

class FileSink (fileName: String, fileDelim:String = ",") extends BasicSinkConnector[Row] {
  private lazy val log = LoggerFactory.getLogger(this.getClass)
  @BeanProperty var mode: WriteMode = WriteMode.OVERWRITE
  @BeanProperty var runPostProcess: Boolean = false
  @BeanProperty var completedDir: String = "file:///tmp"
  val INTERVAL = 1000

  override def loader(env: ExecutionEnvironment, dataSet: DataSet[Row]): Unit = {
    dataSet.rebalance().writeAsText(fileName, mode)
  }
}