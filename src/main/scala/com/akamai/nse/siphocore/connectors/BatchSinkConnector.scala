package com.akamai.nse.siphocore.connectors

import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import org.apache.flink.api.java.operators.DataSink
import org.apache.flink.api.scala.{DataSet}
import org.apache.flink.types.Row

trait BatchSinkConnector[T] extends AbstractServiceTrait with Serializable {
  def loader(dataSet: DataSet[T]):DataSink[Row]
}




