package com.akamai.nse.siphocore.connectors.flink.sink

import com.akamai.nse.siphocore.connectors.BatchSinkConnector
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty

class JdbcSink(dataSource: BasicDataSource, sql: String) extends BatchSinkConnector[Row] {

  val BATCH_SIZE = 500

  @BeanProperty var schema : Array[Int] = _
  @BeanProperty var rowType: RowTypeInfo = _

  private lazy val logger = LoggerFactory.getLogger(this.getClass)
  logger.info(" sql "+sql)
  private def getOutputBuilder:JDBCOutputFormat = {
    JDBCOutputFormat.buildJDBCOutputFormat()
      .setDrivername(dataSource.getDriverClassName)
      .setDBUrl(dataSource.getUrl)
      .setUsername(dataSource.getUsername)
      .setPassword(dataSource.getPassword)
      .setQuery(sql)
      .setBatchInterval(BATCH_SIZE)
      .setSqlTypes(schema)
      .finish()
  }

   def loader(ds: DataSet[Row]):DataSink[Row] = {
     logger.info("Loading data on to the Target table")
    val outputFormatter = getOutputBuilder

     //logger.info("ds "+ds.print())
    ds.output(outputFormatter)
   }

}
