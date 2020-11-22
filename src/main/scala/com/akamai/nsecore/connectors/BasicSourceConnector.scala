package com.akamai.nsecore.Connectors

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

trait BasicSourceConnector[T] extends Serializable {
  def generate(env: ExecutionEnvironment): DataSet[T]
}
