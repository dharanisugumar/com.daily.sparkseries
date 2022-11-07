package com.akamai.nse.siphocore.connectors.jdbc

/** Package for SqlQuery
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

abstract class SqlQuery() {
  val logMessage: String
  val sqlStatement: String
}

