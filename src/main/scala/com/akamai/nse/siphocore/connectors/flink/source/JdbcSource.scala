package com.akamai.nse.siphocore.connectors.flink.source

import java.sql.ResultSet
import java.io.Serializable
import com.akamai.nse.siphocore.common.{TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.connectors.BatchSourceConnector
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import org.apache.flink.api.scala.DataSet
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.common.typeinfo.BasicTypeInfo
import org.apache.flink.api.java.io.jdbc.split.GenericParameterValuesProvider
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.types.Row
import scala.beans.BeanProperty

/** Package for FileSink
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

class JdbcSource(dataSource: BasicDataSource, sql: String,queryParameters:Array[Array[Serializable]]) extends BatchSourceConnector[Row] {


  val FETCH_SIZE = 1000
  val BATCH_SIZE = 500


  @BeanProperty var schema: Array[Int] = _
  //@BeanProperty var rowType: RowTypeInfo = new RowTypeInfo(rowType)
  @BeanProperty var fetchSize: Int = FETCH_SIZE
  //@BeanProperty var queryParameters: Array[Array[Serializable]] = _

  val paramProvider = new GenericParameterValuesProvider(queryParameters)

  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " source - jdbc", TaskStatusEnum.STARTED, "", ""))

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
       BasicTypeInfo.STRING_TYPE_INFO,
       BasicTypeInfo.STRING_TYPE_INFO
       ))
      .setParametersProvider(paramProvider)
      .setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE)
    .finish

  getInputFormat.openInputFormat()

  /* this method fetches the data from the jdbc in batches.
*/

  override def generate(env: ExecutionEnvironment): DataSet[Row] = {
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " sink - jdbc", TaskStatusEnum.FINISHED, "", ""))
    env.createInput(getInputFormat)
  }

}