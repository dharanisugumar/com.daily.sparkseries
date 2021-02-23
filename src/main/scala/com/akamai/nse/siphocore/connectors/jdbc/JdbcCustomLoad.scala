package com.akamai.nse.siphocore.connectors.jdbc

import java.io.FileNotFoundException
import java.sql.{Connection, DriverManager, SQLException}
import java.util.Properties
import java.sql.Types

import com.akamai.nse.siphocore.common.{TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.exception.{SiphoClassNotFoundException, SiphoFileException, SiphoSqlException}
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.beans.BeanProperty
import scala.collection.mutable
import scala.collection.immutable.ListMap
import scala.io.Source


@Component
@Autowired
class JdbcCustomLoad(username: String, password: String, driver: String, dbURL: String, file: String, field: String) extends Table {


  @BeanProperty var retry: Int = 3
  @BeanProperty var chunk: Int = 50


  appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "JDBC custom connection ", TaskStatusEnum.STARTED, "", ""))

  private lazy val logger = LoggerFactory.getLogger(this.getClass)


  var listMap: ListMap[String, String] = ListMap.empty
  var connection: Connection = null
  try Class.forName("oracle.jdbc.driver.OracleDriver")
  catch {
    case e: ClassNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Where is your Oracle JDBC Driver?", TaskStatusEnum.FAILED, "", ""))
        SiphoClassNotFoundException(e.getMessage,this.getClass().getName,"connection","")
  }


  val props = new Properties
  props.setProperty("user", username)
  props.setProperty("password", password)

  connection = DriverManager.getConnection(dbURL,props)
  var stmt = connection.createStatement()

  def buildQuery(fileName: String, field: String) = {

    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "preparing jdbc statement query ", TaskStatusEnum.STARTED, "", ""))

    var qry = ""
    try {
      val file = Source.fromFile(fileName)
      qry = file.mkString.replaceAllLiterally(s"{process_id}", field).replaceAllLiterally("{retry_num}", retry.toString).replaceAllLiterally("{max_chunk_num}", chunk.toString)
    }catch {
      case ex:FileNotFoundException =>
        throw SiphoFileException(ex.getMessage,this.getClass().getName,"buildQuery",fileName)
    }
    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "preparing jdbc statement query ", TaskStatusEnum.FINISHED, "", ""))

    qry
  }

  override def truncate(): ListMap[String,String] = {

    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Truncating tables  ", TaskStatusEnum.STARTED, "", ""))

    val sql = buildQuery(file,"key")
    val sqlArr = sql.split(";")
    try {
      for (sql <- sqlArr) {
        stmt.executeQuery(sql)
        appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Truncating tables  $sql", TaskStatusEnum.FINISHED, "", ""))
      }
      listMap += (field -> "success")
    }catch {
        case e: SQLException =>
          SiphoSqlException(e.getMessage,this.getClass().getName,"truncate")
          appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
          listMap += (field -> "fail")
      }
    listMap
  }

  override def select(): ListMap[String,String] = {

    val sql = buildQuery(file,"")
    var list = new mutable.MutableList[String]()
    try{
    val rs = stmt.executeQuery(sql)
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Query ${field} Executed!  ", TaskStatusEnum.INPROGRESS, "", ""))
      while (rs.next()) {
      val queue = rs.getString(field)
      list += queue
        appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"field : $queue  - ${list.mkString(",")} ", TaskStatusEnum.INPROGRESS, "", ""))
    }
      listMap += (field -> list.mkString(","))
    }catch {
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"select")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }

  override def filter(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    var list = new mutable.MutableList[String]()
    try {
      val rs = stmt.executeQuery(sql)
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Query ${field} Executed!  ", TaskStatusEnum.INPROGRESS, "", ""))
      val fields = field.split(",")
      while (rs.next()) {
        if (fields.size > 1) {
          for (col <- fields) {
            val queue = rs.getString(col)
            appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"queue   ${rs.getDate(col)}  ", TaskStatusEnum.INPROGRESS, "", ""))
            listMap += (col -> queue)
          }
        } else {
          val queue = rs.getString(field)
          list += queue
        }
      }
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"field : $field  - ${list.mkString(",")} ", TaskStatusEnum.INPROGRESS, "", ""))
      listMap += (field -> list.mkString(","))
    }catch {
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"filter")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }


  override def insert(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"query - $sql ", TaskStatusEnum.INPROGRESS, "", ""))
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Executing Stored Procedure  ", TaskStatusEnum.STARTED, "", ""))
      val statement = connection.prepareCall(sql)
      statement.setInt(1,key.toInt)
      statement.registerOutParameter(2, Types.VARCHAR)
      statement.execute()
      val outParam = statement.getString(2)
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Stored Procedure Executed! and output parameter : ${outParam} ", TaskStatusEnum.FINISHED, "", ""))
      listMap += (field -> "success")
    }catch{
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"insert")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }

  override def update(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    try{
    stmt.executeUpdate(sql)
      listMap += (field -> "success")
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Query ${field} Executed!  ", TaskStatusEnum.FINISHED, "", ""))
    }
    catch {
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"update")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }
}

