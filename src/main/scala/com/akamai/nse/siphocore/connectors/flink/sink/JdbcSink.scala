package com.akamai.nse.siphocore.connectors.flink.sink

import com.akamai.nse.siphocore.common.{TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.connectors.BatchSinkConnector
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import org.apache.commons.dbcp.BasicDataSource
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.java.typeutils.RowTypeInfo
import org.apache.flink.api.scala.DataSet
import org.apache.flink.types.Row
import scala.beans.BeanProperty

class JdbcSink(dataSource: BasicDataSource, sql: String) extends BatchSinkConnector[Row] {

  val BATCH_SIZE = 500

  @BeanProperty var schema : Array[Int] = _
  @BeanProperty var rowType: RowTypeInfo = _

  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " sink - jdbc", TaskStatusEnum.STARTED, "", ""))

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
    val outputFormatter = getOutputBuilder
     appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, " sink - jdbc", TaskStatusEnum.FINISHED, "", ""))
     ds.output(outputFormatter)
   }

}
