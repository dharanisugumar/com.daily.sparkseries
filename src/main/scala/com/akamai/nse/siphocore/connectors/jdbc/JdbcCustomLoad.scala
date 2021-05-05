package com.akamai.nse.siphocore.connectors.jdbc

import java.io.FileNotFoundException
import java.sql.{Connection, DriverManager, SQLException}
import java.util.Properties
import java.sql.Types
import net.liftweb.json._
import com.akamai.nse.siphocore.common.{JsonUtil, TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.exception.{SiphoClassNotFoundException, SiphoFileException, SiphoSqlException}
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import com.akamai.nse.siphocore.model.ProcedureInfo
import net.liftweb.json.DefaultFormats
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.beans.BeanProperty
import scala.collection.mutable
import scala.collection.immutable.ListMap
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/** Package for JdbcCustomLoad
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

@Component
@Autowired
class JdbcCustomLoad(username: String, password: String, driver: String, dbURL: String, file: String, field: String, version: String) extends Table {


  @BeanProperty var retry: Int = 3
  @BeanProperty var chunk: Int = 50


  appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "JDBC custom connection ", TaskStatusEnum.STARTED, "", ""))

  var listMap: ListMap[String, String] = ListMap.empty
  var connection: Connection = null
  try Class.forName("oracle.jdbc.driver.OracleDriver")
  catch {
    case e: ClassNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Where is your Oracle JDBC Driver?", TaskStatusEnum.FAILED, "", ""))
        SiphoClassNotFoundException(e.getMessage,this.getClass().getName,"connection","")
  }

  /* connecting to jdbc database
  */
  val props = new Properties
  props.setProperty("user", username)
  props.setProperty("password", password)
  props.setProperty("oracle.net.ssl_version",version)

  appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Setting up properties for jdbc  ", TaskStatusEnum.FINISHED, "", ""))


  connection = DriverManager.getConnection(dbURL,props)
  var stmt = connection.createStatement()

  /* this method :- buildQuery reads the file and replaces the parameter with the values */
  def buildQuery(fileName: String, field: String): String = {

    var qry = ""
    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"filename ${fileName} and field ${field} ", TaskStatusEnum.FINISHED, "", ""))
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "logger inside else  ", TaskStatusEnum.FINISHED, "", ""))
      val file = Source.fromFile(fileName)
        qry = file.mkString.replaceAllLiterally(s"{process_id}", field).replaceAllLiterally("{retry_num}", retry.toString).replaceAllLiterally("{max_chunk_num}", chunk.toString)
      } catch {
      case ex:FileNotFoundException =>
        throw SiphoFileException(ex.getMessage,this.getClass().getName,"buildQuery",fileName)
    }
    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"preparing jdbc statement query $qry", TaskStatusEnum.FINISHED, "", ""))

    qry
  }
  /* this method :- truncate the records from the table */

  override def truncate(): ListMap[String,String] = {

    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Truncating tables  ", TaskStatusEnum.STARTED, "", ""))

    val sql = buildQuery(file,"key")
    val sqlArr = sql.split(";")
    try {
      for (sql <- sqlArr) {
        appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Deleting tables $sql ", TaskStatusEnum.STARTED, "", ""))
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

  /* this method :- select the particular field and store it in list */

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

  /* this method :- filter - fetches the records in a loop and update to the list */

  override def filter(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    var list = new mutable.MutableList[String]()
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"sql $sql  ", TaskStatusEnum.INPROGRESS, "", ""))
      val rs = stmt.executeQuery(sql)
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Query ${field} Executed!  ", TaskStatusEnum.INPROGRESS, "", ""))
      val fields = field.split(",")
      while (rs.next()) {
        if (fields.size > 1) {
          for (col <- fields) {
            val queue = rs.getString(col)
            appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"queue   ${rs.getDate(col)}  ", TaskStatusEnum.INPROGRESS, "", ""))
            listMap += (col -> queue)
          }
        } else {
          val queue = rs.getString(field)
          list += queue
        }
      }
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"field : $field  - ${list.mkString(",")} ", TaskStatusEnum.INPROGRESS, "", ""))
      listMap += (field -> list.mkString(","))
    }catch {
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"filter")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }

  /* this method :- insert - insert the records to the table */

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

  /* this method :- update the record to the table */


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

  override def execute(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"query - $sql ", TaskStatusEnum.INPROGRESS, "", ""))
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Executing Stored Procedure  ", TaskStatusEnum.STARTED, "", ""))

      var procName = ""
      var inputSize = 0
      var outputSize = 0
      var prepCall = "?"
      var inKVMap = Map(1->1)
      var dbName = ""
      var in = 2
      var i = 1

      implicit val formats = DefaultFormats
      val json = parse(sql)
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"json ${json} ", TaskStatusEnum.STARTED, "", ""))
      val procVal = List(key.toInt , retry , chunk)

      //println("json "+json)
      val elements = (json \\ "procedureInfo").children
      for (proc <- elements) {
        val m = proc.extract[ProcedureInfo]
        inputSize = m.inputs.size
        outputSize = m.outputs.size
        dbName = m.db_name
        procName = m.proc_name

      }

      var procKey = ArrayBuffer(1)
      while(in <= inputSize) {
        procKey+=in
        in=in+1
      }
      inKVMap = (procKey zip procVal).toMap
      val outSize = inputSize + outputSize

      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"procname ${procName} and outSize ${outSize} and}", TaskStatusEnum.STARTED, "", ""))

      while( i < outSize){
        prepCall += ",?"
        i=i+1
      }

      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"prepcall ${prepCall}", TaskStatusEnum.STARTED, "", ""))

      val stmt =  s"CALL $dbName.$procName(${prepCall})"

      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"stmt ${stmt}", TaskStatusEnum.STARTED, "", ""))

      val statement = connection.prepareCall(stmt)

      for((k,v)<-inKVMap){
        appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"stmt ${stmt}", TaskStatusEnum.STARTED, "", ""))
        appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"k ${k} and v ${v}", TaskStatusEnum.STARTED, "", ""))
        statement.setInt(k,v)
      }
      statement.registerOutParameter(outSize, Types.VARCHAR)

      statement.execute()
      val outParam = statement.getString(outSize)
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Stored Procedure Executed! and output parameter : ${outParam} ", TaskStatusEnum.FINISHED, "", ""))
      listMap += (field -> "success")
    }catch{
      case e: SQLException =>
        SiphoSqlException(e.getMessage,this.getClass().getName,"execute")
        appContext.jobLogger.eventLog(LogLevelEnum.ERROR, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Could not execute query ${e.getMessage}", TaskStatusEnum.FAILED, "", ""))
        listMap += (field -> "fail")
    }
    listMap
  }
}

