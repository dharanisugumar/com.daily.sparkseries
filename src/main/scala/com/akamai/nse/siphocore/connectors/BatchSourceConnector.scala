package com.akamai.nse.siphocore.connectors

import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}

trait BatchSourceConnector[T] extends AbstractServiceTrait with Serializable {
  def generate(env: ExecutionEnvironment): DataSet[T]
}
