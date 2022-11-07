package com.akamai.nse.siphocore.connectors

import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

trait StreamSourceConnector[T] extends AbstractServiceTrait with Serializable {

    def generate(env: StreamExecutionEnvironment): DataStream[T]

}
