package com.akamai.nse.siphocore.common

/** Package for Appcontext
 * @author  : Dharani
 * @version : 1.0
 */

import com.akamai.nse.siphocore.logger.JobLogger
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

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

  /**
   * This Appcontext mainly helps in customizing and to pick the values of the job id,name,desc,version,active status from the application.conf
   */

  private lazy val logger = LoggerFactory.getLogger(this.getClass)
  val jobLogger: JobLogger = new JobLogger(logger)
  val logIdentifier: String = new SimpleIdGenerator().generator("l")

  protected def getConfig(): Config = ConfigFactory.load()

  def apply(): AppContext = {

    lazy val config = getConfig()

    /**
     * Fetching values from application.conf
     */

    val envJobId = config.getString("JOB_ID").trim
    val envJobName = config.getString("JOB_NAME").trim
    val envJobDesc = config.getString("JOB_DESC").trim
    val envJobVersion = config.getString("JOB_VERSION").trim
    val envJobActive = config.getString("JOB_ACTIVE")

    /**
     * calling the job constructor for the custom logger
     */

    val job: Job = Job(envJobId, envJobName, envJobDesc, envJobVersion, envJobActive)

    new AppContext(config,jobLogger,job,logIdentifier)

  }
}
