package com.akamai.nse.siphocore.common

import org.apache.flink.api.common.JobExecutionResult

trait FlinkExecutor {
  def execute(): JobExecutionResult
}
