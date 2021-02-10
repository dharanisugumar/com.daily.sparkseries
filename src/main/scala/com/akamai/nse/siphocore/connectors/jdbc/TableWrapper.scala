package com.akamai.nse.siphocore.connectors.jdbc

abstract class TableWrapper(val databaseName: String) {

  type T_SqlType <: SqlQuery
  val logicalName: String
  val physicalName: String
  val tableColumns: Array[(String, String)]
  //val schema: StructType
  val createTableOpts: String
  val tableDescription: String = ""

  //final val databaseName = _databaseName

  final lazy val tableName = s"${databaseName}.${physicalName}"
  final lazy val tableSchema: String = tableColumns.map(tableColumn => s"${tableColumn._1} ${tableColumn._2}").mkString(",\n")

  lazy val sqlCreateTableStatement = s"CREATE TABLE IF NOT EXISTS ${tableName} (${tableSchema}) ${createTableOpts}"
  lazy val sqlCreateTableLogMessage = s"Creating table if not exists: ${tableName}"
  def sqlCreateTable: T_SqlType

  lazy val sqlDropTableStatement = s"DROP TABLE IF EXISTS ${tableName} PURGE"
  lazy val sqlDropTableLogMessage = s"Droping table: ${tableName}"
  def sqlDropTable: T_SqlType

  lazy val sqlTruncateTableStatement = s"TRUNCATE TABLE  ${tableName}"
  lazy val sqlTruncateTableLogMessage = s"Truncating entire table: ${tableName}"
  def sqlTruncateTable: T_SqlType

  lazy val sqlCalculateStatisticsStatement = s"ANALYZE TABLE ${tableName} COMPUTE STATISTICS"
  lazy val sqlCalculateStatisticsLogMessage = s"Calculating statistics for table: ${tableName}"
  def sqlCalculateStatistics: T_SqlType

  def sqlInsertTableStatement(sqlStatement: String) = s"INSERT INTO ${tableName} ${sqlStatement}"
  def sqlInsertTableLogMessage(logMessage: String) = s"Inserting into table: ${tableName} --> ${logMessage}"
  def sqlInsertTable(sqlQuery: T_SqlType): T_SqlType

  //lazy val sqlSelectStatement = s"SELECT ${schema.fields.map(_.name).mkString(",")} from ${tableName}"
}
