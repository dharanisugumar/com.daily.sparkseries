package com.akamai.nse.siphocore.logger

import com.akamai.nse.siphocore.common.{Job, TimestampConverter}
import com.akamai.nse.siphocore.logger.LogLevelEnum.LogLevel
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger

/** Package for JobLogger
 * @author  : Raju Gurumurthy
 * @version : 1.0
 */

class JobLogger(logger:Logger) extends LoggerTrait with Serializable {

  def eventLog(logLevel:LogLevel,job: Job,logMsg :LogMessage): Unit = {

    val logMessage = s"App - ${job.jobId} ${job.jobName} version ${job.jobVersion}  : Log Identifier - ${logMsg.eventId} ${logMsg.eventDescription} ${logMsg.eventType} Stage - ${logMsg.stageName} Step - ${logMsg.stepName} ${logMsg.status} ${logMsg.eventOutPut} ${logMsg.message} "
    if (logLevel == LogLevelEnum.INFO) this.info(s"${logMessage}")
    else if(logLevel == LogLevelEnum.ERROR) this.error(s"${logMessage}")
    else if(logLevel == LogLevelEnum.DEBUG) this.debug(s"${logMessage}")
    else if(logLevel == LogLevelEnum.WARN) this.warn(s"${logMessage}")
  }

      def logError(e: Exception): Boolean = {
        this.add((stackTrace, e.toString()))
        this.error(ExceptionUtils.getStackTrace(e))
        true
      }


}










