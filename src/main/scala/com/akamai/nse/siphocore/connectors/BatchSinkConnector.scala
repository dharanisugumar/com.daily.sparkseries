package com.akamai.nse.siphocore.connectors

import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.types.Row
trait BatchSinkConnector[T] extends Serializable {
  def loader(dataSet: DataSet[T]):DataSink[Row]
}




