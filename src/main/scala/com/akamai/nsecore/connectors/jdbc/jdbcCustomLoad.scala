package com.akamai.nsecore.connectors.jdbc


import java.sql.{Connection, SQLException}
import org.slf4j.LoggerFactory
import scala.collection.mutable.ListBuffer
import scala.io.Source

class jdbcCustomLoad(username: String, password: String,driver: String, dbURL: String,file:String)

{
  private lazy val logger = LoggerFactory.getLogger(this.getClass)


  var connection: Connection = null

    try {

    } catch {
      case sqe: SQLException =>
        throw new IllegalArgumentException("open() failed.", sqe)
      case cnfe: ClassNotFoundException =>
        throw new IllegalArgumentException("JDBC driver class not found.", cnfe)
    }


    val sql = buildQuery(file)
    val stmt = connection.createStatement()
    var list = new ListBuffer[String]()
    try {
      val rs = stmt.executeQuery(sql)
      while (rs.next()) {
        val queue = rs.getString("queue")
        list+=queue
      }
      val queryParameters = new Array[Array[String]](list.size)
      var i =0
      for (acc <- list) {
        queryParameters(i) = Array[String](acc)
        i += 1
      }
      queryParameters
    } catch {
      case e: SQLException =>
        logger.error(s" ${e.getMessage}")
    }
    try {
      stmt.close()
    } catch {
      case e: SQLException =>
        logger.warn(s"Could not close JDBC connection after load file ${file}: ${e.getMessage}")
    }
  if (this.connection != null) try this.connection.close()
  catch {
    case var5: SQLException =>
      logger.warn("JDBC connection could not be closed: " + var5.getMessage)
  } finally this.connection = null

  def buildQuery(fileName: String): String = {
    val file = Source.fromFile(fileName)
    file.mkString

  }

}