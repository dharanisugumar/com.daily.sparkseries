package com.akamai.nsecore.common

import org.apache.flink.api.common.JobExecutionResult

trait FlinkExecutor {
  def execute(): JobExecutionResult
}
