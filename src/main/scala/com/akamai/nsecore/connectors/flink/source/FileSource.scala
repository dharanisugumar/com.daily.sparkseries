package com.akamai.nsecore.Connectors.Source

import com.akamai.nsecore.Connectors.BasicSourceConnector
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory
import scala.beans.BeanProperty


class FileSource (fileName: String, fileDelim:String = ",") extends BasicSourceConnector[Row] {
  private lazy val log = LoggerFactory.getLogger(this.getClass)
  @BeanProperty var mode: WriteMode = WriteMode.OVERWRITE
  @BeanProperty var runPostProcess: Boolean = false
  @BeanProperty var completedDir: String = "file:///tmp"
  val INTERVAL = 1000


  override def generate(env: ExecutionEnvironment): DataSet[Row] = {
    val readFile: DataSet[String] = env.readTextFile(fileName)
    val ds = readFile.map(line => line.split(fileDelim)).map(a => Row.of(a: _*))
    ds
  }
}
