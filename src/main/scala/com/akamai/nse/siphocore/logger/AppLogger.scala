package com.akamai.nse.siphocore.logger

import java.util.UUID
import com.akamai.nse.siphocore.common.TimestampConverter
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.{Logger, LoggerFactory}

/** Package for AppLogger
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

class AppLogger(logger:Logger) extends LoggerTrait {

  final val statusSuccess = "SUCCESS"
  final val statusFailure = "FAILURE"
  final val statusWarning = "WARNING"

  private def getCurrentTimestamp: String = {
     new TimestampConverter().UTCText("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  }

  private def getDefaultVersionLong: String = {
     "1.0.0"
  }

  def startPipeline(pipelineNameVal: String,

                    ): Unit = {

    val eventMessage = s"Starting pipeline"
    val currentTimestamp = new TimestampConverter().UTCText("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val uniqNo = UUID.randomUUID().toString
    this.add((pipelineName, pipelineNameVal))
    this.add((pipelineVersion, getDefaultVersionLong))
    this.add((eventType, s"${eventMessage}"))
    this.add((eventTimestamp, currentTimestamp))
    this.add((startTime, currentTimestamp))
    this.remove(eventOutcome)
    this.remove(endTime)
    this.remove(stageName)
    this.remove(stepName)
    this.add(uniqueRequestId, uniqNo)
    val logMessage = s"${pipelineNameVal} : ${pipelineNameVal}${eventMessage} "
    //logger.module(s"${pipelineNameVal}")
    this.info(s"${logMessage}")
  }

  def finishPipeline(eventOutcomeVal: String = statusSuccess): Unit = {

    val eventMessage = s"Finishing pipeline"

    this.add((eventType, s"${eventMessage}"))
    this.add((eventTimestamp, getCurrentTimestamp))
    this.add((endTime, getCurrentTimestamp))
    this.add((eventOutcome, s"${eventOutcomeVal}"))
    val logMessage = s"${eventMessage} with status ${eventOutcomeVal}"

    if (eventOutcomeVal.equals(statusFailure)) {
      this.error(s"${logMessage}")
    } else if (eventOutcomeVal.equals(statusWarning)) {
      this.warn(s"${logMessage}")
    } else {
      this.info(s"${logMessage}")
    }

  }

  def logMessage(logMessage: String, detailedInfo: String = ""): Boolean = {
        this.add((eventType, logMessage))
        this.add((eventTimestamp, getCurrentTimestamp))
        this.info(s"${detailedInfo}")
        true
  }

  def logError(e: Exception): Boolean = {
        this.add((stackTrace, e.toString()))
        this.error(ExceptionUtils.getStackTrace(e))
        true
  }
}

object AppLogger {
    val DEFAULT_STEP_TYPE = "transformation"
    def getLogger(): AppLogger = {
      new AppLogger(LoggerFactory.getLogger(getClass.getName))
    }
}








