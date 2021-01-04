package com.akamai.nse.siphocore.connectors.jdbc

import java.sql.{Connection, DriverManager, ResultSet, SQLException, Statement}
import java.util.Properties

import com.akamai.nse.siphocore.connectors.flink.source.JdbcSource
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

  //logger.info(s" keystore $keyStore and $keyStoreType and $username")

  var listMap: ListMap[String, String] = ListMap.empty
  var connection: Connection = null
  try Class.forName("oracle.jdbc.driver.OracleDriver")
  catch {
    case e: ClassNotFoundException =>
      System.out.println("Where is your Oracle JDBC Driver?")
      e.printStackTrace()

  }
  val props = new Properties
  props.setProperty("user", username)
  props.setProperty("password", password)

  connection = DriverManager.getConnection(dbURL,props)
  val stmt = connection.createStatement()

  def buildQuery(fileName: String, field: String): String = {


    val file = Source.fromFile(fileName)
    val qry = file.mkString.replaceAllLiterally(s"{process_id}", field).replaceAllLiterally("{retry_num}", retry.toString).replaceAllLiterally("{max_chunk_num}", chunk.toString)
    logger.info("Query to Execute: "+qry)
    qry
}



  override def truncate(): ListMap[String,String] = {

    val sql = buildQuery(file,"key")
    val sqlArr = sql.split(";")
    for (sql <- sqlArr) {
      try{
      stmt.executeQuery(sql)
      logger.info(s" Truncating Stage Table Successful :$sql")}
      catch {
        case e: SQLException =>
          logger.error(s"Could not execute query ${e.getMessage}")
      }
    }
    listMap += (field -> "success")
    listMap
  }

  override def select(): ListMap[String,String] = {
    val sql = buildQuery(file,"")
    var list = new mutable.MutableList[String]()
    try{
    val rs = stmt.executeQuery(sql)
    logger.info(s" ${field} Query Executed! ")

    while (rs.next()) {
      val queue = rs.getString(field)
      if(queue == null){
        logger.info(s" Bex Preprocess : Skip :  ${field.toUpperCase()} does not exist!! ") //Module-name status
        System.exit(0)
      }
        //throw new NullPointerException(



      list += queue
      logger.info(field +":" + queue)
    }}catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
    }
    listMap += (field -> list.mkString(","))
    listMap
  }

  override def filter(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)

    var list = new mutable.MutableList[String]()
    try {
      val rs = stmt.executeQuery(sql)
      logger.info(s"  ${field}  Query Executed! ")

      val fields = field.split(",")
      logger.info(" fields " + fields.foreach(println))

      while (rs.next()) {
        if (fields.size > 1) {
          for (col <- fields) {
            val queue = rs.getString(col)
            //list += queue
            logger.info(" queue " + rs.getDate(col))
            listMap += (col -> queue)
          }
        } else {
          val queue = rs.getString(field)
          list += queue
          logger.info(" list " + list)
        }
      }
    }catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
    }
    listMap += (field -> list.mkString(","))
    //logger.info(field + list.mkString(","))
    listMap
  }


  override def insert(key:String): ListMap[String,String] = {

    val sql = buildQuery(file,key)
    connection.prepareCall(sql).execute()
    //try{
    //stmt.p(sql)
    //
//    catch {
//      case e: SQLException =>
//        logger.error(s"Could not execute query ${e.getMessage}")
//    }
    logger.info(s" EXECUTED ${field} SQL: ${"success"}")
    listMap += (field -> "success")
    listMap}

  override def update(key:String): ListMap[String,String] = {
    val sql = buildQuery(file,key)
    try{
    stmt.executeUpdate(sql)}
    catch {
      case e: SQLException =>
        logger.error(s"Could not execute query ${e.getMessage}")
    }
    logger.info(s" EXECUTED ${field} SQL: ${"success"}")
    listMap += (field -> "success")
    listMap
  }

}

