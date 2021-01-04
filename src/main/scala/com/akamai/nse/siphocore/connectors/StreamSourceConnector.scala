package com.akamai.nse.siphocore.connectors

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

trait StreamSourceConnector [T] extends Serializable {

    def generate(env: StreamExecutionEnvironment): DataStream[T]

}
