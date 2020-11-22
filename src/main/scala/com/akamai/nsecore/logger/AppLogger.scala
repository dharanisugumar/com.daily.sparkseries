package com.akamai.nsecore.logger

import java.util.logging.Level

import com.typesafe.config.ConfigFactory

class AppLogger {

  lazy val config = ConfigFactory.load()
  lazy val lifecycle = config.getString("ENV_LIFECYCLE_LONG").trim
  lazy val esIndex = config.getString("ES_INDEX")
  lazy val esUrl = config.getString("ES_URL")
  lazy val appCode = config.getString("ENV_APP_CD")
  lazy val envUser = config.getString("ENV_USER")

  val defaults = Map[EventAttribute, String](
    pipelineLifecycle -> lifecycle,
    stageLifecycle -> lifecycle,
    stepLifecycle -> lifecycle,
    externalEventLifecycle -> lifecycle,
    correlationUUID -> "test-UUID",
    logTimestamp -> CommonUtils.timestamp,
    eventTimestamp -> CommonUtils.timestamp,
    //businessTransactionId -> "12345",  Its not getting resolved with version 1.3.4
    //startTime -> CommonUtils.timestamp,
    //endTime -> CommonUtils.timestamp,
    componentIdentifier -> "IE9",
    componentVersion -> "4.0.0",
    componentLifecycle -> lifecycle,
    componentService -> "Format",
    countryScope -> "IE",
    // serviceScope -> "NDWx",  Its not getting resolved with version 1.3.4
    assetScope -> "IE9",
    supplierScope -> "TDW",
    productScope -> "LRX" ,
    clientScope -> "ALL" ,
    user -> envUser
  )

  def log (evntLvl: Level, stgName: String, stepNm: String, cmpntService: String, evntTyp: String
           , evntOutcm: String, msg: String) = {
    logger.addOptions(Map[EventAttribute, String](
      stageName -> stgName,
      stepName -> stepNm,
      componentService -> cmpntService,
      eventType -> evntTyp,
      eventOutcome -> evntOutcm
    ))
    evntLvl match {
      case Level.INFO  => logger.info(msg)
      case Level.ERROR => logger.error(msg)
      case Level.DEBUG => logger.debug(msg)
      case Level.WARN  => logger.warn(msg)
      case Level.FATAL => logger.fatal(msg)
      case Level.TRACE => logger.trace(msg)
    }
  }

  def setLogLevels(level: Level, loggers: Seq[String]): Unit = {
    loggers.foreach(loggerName => {Logger.getLogger(loggerName).setLevel(level)})
  }





}
