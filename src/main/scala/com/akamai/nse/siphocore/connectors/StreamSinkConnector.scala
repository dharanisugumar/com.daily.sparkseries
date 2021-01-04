package com.akamai.nse.siphocore.connectors

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.DataStream

trait StreamSinkConnector[T] extends Serializable {
  def loader(env: StreamExecutionEnvironment,ds: DataStream[T]): Unit
}
