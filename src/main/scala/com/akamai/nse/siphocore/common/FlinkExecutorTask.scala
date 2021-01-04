package com.akamai.nse.siphocore.common

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.springframework.context.ApplicationContext

trait FlinkExecutorTask extends Serializable {
  def task(env: Either[StreamExecutionEnvironment, ExecutionEnvironment], args: Array[String])
          (implicit ctx: ApplicationContext): FlinkExecutor

}