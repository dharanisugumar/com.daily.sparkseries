package com.akamai.nse.siphocore.common

/** Package for FlinkExecutorTask
 * @author  : Dharani
 * @version : 1.0
 */

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.springframework.context.ApplicationContext

/**
 * trait  to define a flink job
 */
trait FlinkExecutorTask extends Serializable {

  /**
   * This method :- task basically executes the flink job in both stream and batch processing
   * as per the env that we choose and the configurations are read from spring bean context xml file
   */

  def task(env: Either[StreamExecutionEnvironment, ExecutionEnvironment], args: Array[String],appContext: AppContext)
          (implicit ctx: ApplicationContext): FlinkExecutor
}