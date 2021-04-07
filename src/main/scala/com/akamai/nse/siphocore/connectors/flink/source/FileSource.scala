package com.akamai.nse.siphocore.connectors.flink.source

import com.akamai.nse.siphocore.connectors.BatchSourceConnector
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory
import scala.beans.BeanProperty

/** Package for FileSource
 * @author  : Dharani Sugumar
 * @version : 1.0
 */
class FileSource (fileName: String, fileDelim:String = ",") extends BatchSourceConnector[Row] {
  private lazy val log = LoggerFactory.getLogger(this.getClass)


  @BeanProperty var mode: WriteMode = WriteMode.OVERWRITE
  @BeanProperty var runPostProcess: Boolean = false
  @BeanProperty var completedDir: String = "file:///tmp"
  val INTERVAL = 1000

  /* this method fetches the data from the file
  */
  override def generate(env: ExecutionEnvironment): DataSet[Row] = {
    val readFile: DataSet[String] = env.readTextFile(fileName)
    val ds = readFile.map(line => line.split(fileDelim)).map(a => Row.of(a: _*))
    ds
  }
}
