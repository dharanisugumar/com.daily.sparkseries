package com.akamai.nsecore.Connectors

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
trait BasicSinkConnector[T] extends Serializable {
  def loader(env: ExecutionEnvironment, dataSet: DataSet[T]): Unit
}




