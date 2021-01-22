package com.akamai.nse.siphocore.connectors.jdbc

import java.io.FileNotFoundException
import java.sql.{Connection, DriverManager, ResultSet, SQLException, Statement}
import java.util.Properties

import com.akamai.nse.siphocore.connectors.flink.source.JdbcSource
import com.akamai.nse.siphocore.exception.{SiphoException, SiphoFileException, SiphoSqlException}
import org.apache.flink.api.scala.{DataSet, GroupedDataSet}
import org.apache.flink.types.Row
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.{Autowired, Required, Value}
import org.springframework.stereotype.Component

import scala.beans.BeanProperty
import scala.collection.{immutable, mutable}
import scala.collection.immutable.ListMap
import scala.collection.mutable.{ListBuffer, MutableList}
import scala.io.Source


@Component
@Autowired
class JdbcCustomLoad(username: String, password: String, driver: String, dbURL: String, file: String, field: String) extends Table {


  @BeanProperty var retry: Int = 3
  @BeanProperty var chunk: Int = 50


  private lazy val logger = LoggerFactory.getLogger(this.getClass)


  var listMap: ListMap[String, String] = ListMap.empty
  var connection: Connection = null
  try Class.forName("oracle.jdbc.driver.OracleDriver")
  catch {
    case e: ClassNotFoundException =>
      System.out.println("Where is your Oracle JDBC Driver?") //to be changed to logger
      e.printStackTrace()

  }


  val props = new Properties
  props.setProperty("user", username)
  props.setProperty("password", password)

  connection = DriverManager.getConnection(dbURL,props)
  val stmt = connection.createStatement()

  def buildQuery(fileName: String, field: String) = {

    var qry = ""
    try {
      val file = Source.fromFile(fileName)
      qry = file.mkString.replaceAllLiterally(s"{process_id}", field).replaceAllLiterally("{retry_num}", retry.toString).replaceAllLiterally("{max_chunk_num}", chunk.toString)
    }catch {
      case ex:FileNotFoundException =>
        throw SiphoFileException(ex.getMessage,this.getClass().getName,"buildQuery",fileName,58)
    }
    qry
  }

  override def truncate(): ListMap[String,String] = {

    val sql = buildQuery(file,"key")
    val sqlArr = sql.split(";")
    try {
      for (sql <- sqlArr) {
        stmt.executeQuery(sql)
        logger.debug(s" Truncating Stage Table Successful :$sql")
      }
      listMap += (field -> "success")
    }catch {
        case e: SQLException =>
          logger.error(s"Could not execute query ${e.getMessage}")
          listMap += (field -> "fail")
      }
    listMap
  }

  override def select(): ListMap[String,String] = {
    val sql = buildQuery(file,"")
    var list = new mutable.MutableList[String]()
    try{
    val rs = stmt.executeQuery(sql)
    logger.debug(s" Query ${field} Executed! ")
    while (rs.next()) {
      val queue = rs.getString(field)
      list += queue
      logger.debug(field +":" + queue +"-"+ list.mkString(","))
    }
      listMap += (field -> list.mkString(","))
    }catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
        listMap += (field -> "fail")
    }
    listMap
  }

  override def filter(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    var list = new mutable.MutableList[String]()
    try {
      val rs = stmt.executeQuery(sql)
      logger.debug(s"  ${field}  Query Executed! ")
      val fields = field.split(",")
      logger.debug(" fields " + fields.foreach(println))
      while (rs.next()) {
        if (fields.size > 1) {
          for (col <- fields) {
            val queue = rs.getString(col)
            logger.debug(" queue " + rs.getDate(col))
            listMap += (col -> queue)
          }
        } else {
          val queue = rs.getString(field)
          list += queue
        }
      }
      logger.debug("Field "+list+"  "+list.mkString(","))
      listMap += (field -> list.mkString(","))
    }catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
        listMap += (field -> "fail")
    }
    listMap
  }


  override def insert(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    try {
      connection.prepareCall(sql).execute()
      listMap += (field -> "success")
      logger.debug(s" EXECUTED ${field} SQL: ${"success"}")
    }catch{
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
        listMap += (field -> "fail")
    }
    listMap
  }

  override def update(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    try{
    stmt.executeUpdate(sql)
      listMap += (field -> "success")
      logger.debug(s" EXECUTED ${field} SQL: ${"success"}")
    }
    catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
        listMap += (field -> "fail")
    }
    listMap
  }
}

