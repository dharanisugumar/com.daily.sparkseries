package com.akamai.nse.siphocore.connectors.jdbc

abstract class SqlQuery() {
  val logMessage: String
  val sqlStatement: String
}

