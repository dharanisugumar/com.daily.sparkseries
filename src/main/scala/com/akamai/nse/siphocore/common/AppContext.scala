package com.akamai.nse.siphocore.common

import java.util.Properties

import com.akamai.nse.siphocore.exception.SiphoFileException
import com.akamai.nse.siphocore.logger.JobLogger
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

import scala.io.Source


case class AppContext(config: Config,jobLogger:JobLogger,job: Job,logIdentifier:String)


/**
 * Companion object to assist construction of AppContext
 */
object AppContext extends AppContextTrait {
}

/**
 * Trait that helps us during testing
 */
trait AppContextTrait {


  private lazy val logger = LoggerFactory.getLogger(this.getClass)
  val jobLogger: JobLogger = new JobLogger(logger)
  val logIdentifier: String = new SimpleIdGenerator().generator("l")

  protected def getConfig(): Config = ConfigFactory.load()

  def apply(): AppContext = {

    lazy val config = getConfig()

    val envJobId = config.getString("JOB_ID").trim
    val envJobName = config.getString("JOB_NAME").trim
    val envJobDesc = config.getString("JOB_DESC").trim
    val envJobVersion = config.getString("JOB_VERSION").trim
    val envJobActive = config.getString("JOB_ACTIVE")

    val job: Job = Job(envJobId, envJobName, envJobDesc, envJobVersion, envJobActive)

    new AppContext(config,jobLogger,job,logIdentifier)

  }
}
