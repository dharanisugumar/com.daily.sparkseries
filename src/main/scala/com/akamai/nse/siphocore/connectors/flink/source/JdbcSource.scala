package com.akamai.nse.siphocore.connectors.flink.source

import java.sql.{Connection, DriverManager, ResultSet}
import java.io.Serializable
import java.util.Properties

import com.akamai.nse.siphocore.connectors.BatchSourceConnector
import org.apache.flink.api.scala.DataSet
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.java.io
import org.apache.flink.api.java.io.jdbc.split.{GenericParameterValuesProvider, ParameterValuesProvider}
import org.apache.flink.api.java.io.jdbc.{JDBCInputFormat, JDBCOutputFormat}
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory

import scala.beans.BeanProperty
import scala.collection.mutable.ListBuffer

class JdbcSource(dataSource: BasicDataSource, sql: String,queryParameters:Array[Array[Serializable]]) extends BatchSourceConnector[Row] {

  private lazy val logger = LoggerFactory.getLogger(this.getClass)

  val FETCH_SIZE = 1000
  val BATCH_SIZE = 500


  @BeanProperty var schema: Array[Int] = _
  //@BeanProperty var rowType: RowTypeInfo = new RowTypeInfo(rowType)
  @BeanProperty var fetchSize: Int = FETCH_SIZE
  //@BeanProperty var queryParameters: Array[Array[Serializable]] = _

  logger.info("queryParameters "+queryParameters.length)

  val paramProvider = new GenericParameterValuesProvider(queryParameters)

  private def getInputFormat: JDBCInputFormat = JDBCInputFormat.buildJDBCInputFormat
      .setDrivername(dataSource.getDriverClassName)
      .setDBUrl(dataSource.getUrl)
      .setUsername(dataSource.getUsername)
      .setPassword(dataSource.getPassword)
      .setQuery(sql)
      .setFetchSize(FETCH_SIZE)
      .setRowTypeInfo(new RowTypeInfo(
       BasicTypeInfo.BIG_DEC_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.DATE_TYPE_INFO,
       BasicTypeInfo.DATE_TYPE_INFO,
       BasicTypeInfo.DATE_TYPE_INFO,
       BasicTypeInfo.DATE_TYPE_INFO,
       BasicTypeInfo.BIG_DEC_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO))
      .setParametersProvider(paramProvider)
      .setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)
    .finish

  getInputFormat.openInputFormat()

  override def generate(env: ExecutionEnvironment): DataSet[Row] = {
    env.createInput(getInputFormat)
    //getInputFormat.closeInputFormat
  }

}