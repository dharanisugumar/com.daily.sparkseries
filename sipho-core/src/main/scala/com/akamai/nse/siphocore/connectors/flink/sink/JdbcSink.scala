package com.akamai.nse.siphocore.connectors.flink.sink

import com.akamai.nse.siphocore.common.{Encryption, TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.connectors.BatchSinkConnector
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.DataSet
import org.apache.flink.types.Row

import scala.beans.BeanProperty

/** Package for JdbcSink
 * @author  : Dharani Sugumar
 * @version : 1.0
 */
class JdbcSink(dataSource: BasicDataSource, sql: String) extends BatchSinkConnector[Row] {

  val BATCH_SIZE = 500

  @BeanProperty var schema : Array[Int] = _
  @BeanProperty var rowType: RowTypeInfo = _

  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " sink - jdbc", TaskStatusEnum.STARTED, "", ""))
  /* this method basically load the data into the jdbc table if the jdbc has been selected as the sink.
   */

  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " Decrypting the password ", TaskStatusEnum.STARTED, "", ""))

  val key = "password"
  val decryptedPwd = Encryption.decrypt(key,dataSource.getPassword)
  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " Decrypting the password ", TaskStatusEnum.FINISHED, "", ""))

  private def getOutputBuilder:JDBCOutputFormat = {
    JDBCOutputFormat.buildJDBCOutputFormat()
      .setDrivername(dataSource.getDriverClassName)
      .setDBUrl(dataSource.getUrl)
      .setUsername(dataSource.getUsername)
      .setPassword(decryptedPwd)
      .setQuery(sql)
      .setBatchInterval(BATCH_SIZE)
      .setSqlTypes(schema)
      .finish()
  }

   def loader(ds: DataSet[Row]):DataSink[Row] = {
    val outputFormatter = getOutputBuilder
     appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " sink - jdbc", TaskStatusEnum.FINISHED, "", ""))
     ds.output(outputFormatter)
   }

}
