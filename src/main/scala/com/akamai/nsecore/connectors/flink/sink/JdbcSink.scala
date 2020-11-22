package com.akamai.nsecore.Connectors.Sink

import com.akamai.nsecore.Connectors.{BasicSinkConnector, BasicSourceConnector}
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.types.Row

import scala.beans.BeanProperty

class JdbcSink(dataSource: BasicDataSource, sql: String) extends BasicSinkConnector[Row] {
  val FETCH_SIZE = 1000
  val BATCH_SIZE = 500

  @BeanProperty var schema : Array[Int] = _
  @BeanProperty var rowType: RowTypeInfo = _
  @BeanProperty var fetchSize: Int = FETCH_SIZE

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

   def loader(env: ExecutionEnvironment, ds: DataSet[Row]): Unit = {
    val outputFormatter = getOutputBuilder
    ds.output(outputFormatter)
  }

}
