package com.akamai.nsecore.connectors.flink.source

import com.akamai.nsecore.Connectors.BasicSourceConnector
import org.apache.flink.api.scala.DataSet
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.java.io.jdbc.{JDBCInputFormat, JDBCOutputFormat}
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.types.Row
import scala.beans.BeanProperty

class JdbcSource(dataSource: BasicDataSource, sql: String) extends BasicSourceConnector[Row] {
  val FETCH_SIZE = 1000
  val BATCH_SIZE = 500

  @BeanProperty var schema: Array[Int] = _
  @BeanProperty var rowType: RowTypeInfo = _
  @BeanProperty var fetchSize: Int = FETCH_SIZE

  private def getInputFormat: JDBCInputFormat = {
    JDBCInputFormat.buildJDBCInputFormat
      .setDrivername(dataSource.getDriverClassName)
      .setDBUrl(dataSource.getUrl)
      .setUsername(dataSource.getUsername)
      .setPassword(dataSource.getPassword)
      .setQuery(sql)
      .setFetchSize(this.getFetchSize)
      .setRowTypeInfo(rowType)
      .finish

  }

  override def generate(env: ExecutionEnvironment): DataSet[Row] = {
    env.createInput(getInputFormat)
  }
}