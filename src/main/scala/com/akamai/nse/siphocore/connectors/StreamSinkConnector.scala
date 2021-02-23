package com.akamai.nse.siphocore.connectors

import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.DataStream

trait StreamSinkConnector[T] extends AbstractServiceTrait with Serializable {
  def loader(env: StreamExecutionEnvironment,ds: DataStream[T]): Unit
}
