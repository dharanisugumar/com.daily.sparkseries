package com.akamai.nse.siphocore.connectors

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

trait BatchSourceConnector[T] extends Serializable {
  def generate(env: ExecutionEnvironment): DataSet[T]
}
