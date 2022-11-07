package com.akamai.nse.siphocore.common

/** Package for FlinkExecutor
 * @author  : Dharani
 * @version : 1.0
 */

import org.apache.flink.api.common.JobExecutionResult

/**
 * trait  to execute flink job
 */
trait FlinkExecutor {

  /**
   * this returns the jobExecutionResult based on the input job id
   */

  def execute(): JobExecutionResult
}
