package com.akamai.nse.siphocore

import java.sql.Connection

import com.akamai.nse.siphocore.common.FlinkExecutor
import com.akamai.nse.siphocore.logger.AppLogger
import com.akamai.nse.siphocore.common.{FlinkExecutor, TimestampConverter}
import com.akamai.nse.siphocore.logger.{AppLogger, EventAttribute}
import com.typesafe.config.Config

import scala.collection.parallel.immutable


case class AppContext(
                       stopSparkContext: () => Boolean,
                       stopJdbcConnection: () => Boolean
                     )


/**
 * Companion object to assist construction of AppContext
 */
object AppContext extends AppContextTrait {
}



/**
 * Trait that helps us during testing
 */

trait AppContextTrait {
  @transient protected var _flink: FlinkExecutor = _
  @transient protected var _JdbcConnection: Connection = _

  protected def stopSparkContext(): Boolean = {
    if (_flink != null) {
     // _flink.stop()
      //      System.clearProperty("spark.driver.port")
      _flink = null
    }
    true
  }

  protected def stopImpalaConnection(): Boolean = {
    if (_JdbcConnection != null) {
      _JdbcConnection.close()
      _JdbcConnection = null
    }
    true
  }

//  def apply(): AppContext = {
//  //  _flink = FlinkExecutor
//    //lazy val config = getConfig()
//
//  }


  protected def getLogger(logFileAppender: Boolean,
                          logFileName: String,
                          logConsoleAppender: Boolean,
                          logFormatDelim: String,
                          logFormatType: String,
                          logSocketAppender: Boolean,
                          logSocketHost: String,
                          logSocketPort: Int,
                          logSocketReconnectDelay: Int,
                          logThreshold: String,
                          logElasticAppender: Boolean,
                          logElasticUrl: String,
                          logElasticType: String,
                          logMaxLines: Int,
                          envApplicationCode: String,
                          envTenantCode: String,
                          envLifecycleShort: String,
                          envLifecycleLong: String,
                          envDefaultLogLevel: String,
                          applicationId: String)(logElasticIndexName: String): AppLogger = {

    val logger = AppLogger.getLogger()

   /* logger.setEventAttributes(new Settings(
      immutable.Map[EventAttribute, String](
        country -> envTenantCode,
        asset -> envApplicationCode,
        component -> s"loadAsset",
        componentLifecycle -> envLifecycleShort,
        module -> s"addLater: serviceClassName",
        stageName -> s"addLater: processStepName",
        action -> s"addLater: processActionName",
        status -> envDefaultLogLevel,
        correlationUUID -> applicationId,
        uniqueRequestId -> applicationId,
        //RuntimeUI attributes - View #1:
        countryScope -> "FRANCE",
        assetTypeScope -> envApplicationCode.toUpperCase,
        supplierScope -> "-",
        pipelineLifecycle -> envLifecycleLong.capitalize,
        //RuntimeUI attributes - View #2:
        stageLifecycle -> envLifecycleLong.capitalize,
        stepLifecycle -> envLifecycleLong.capitalize,
        stageVersion -> "1.0.0",
        stepVersion -> "1.0.0",
        pipelineName -> s"addLater: pipelineName",
        pipelineVersion -> "1.0.0",
        eventType -> s"", //Starting | Finishing pipeline, Starting | Finishing stage, Starting | Finishing step
        startTime -> s"", //"2018-04-19T08:29:26.478Z"
        endTime -> s"", //"2018-04-19T08:29:26.478Z" - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
        eventOutcome -> s"", //SUCCESS | FAILURE | WARNING
        dataName -> s"", //file XYZ
        dataType -> s"", //File | Table
        stepType -> s"", //"Rule"
        stepName -> s"", //"StageOne",
        stepPath -> s"", // /0:0|Some(StageOne-1.x) or 0:0|Some(StageOne-1.x)/0:1|Some(StepTwo-1.x)

        //RuntimeUI attributes - View #3:
        stepDescription -> "", //Description of the executed step (metadata)
        recordCount -> "" //# of records processed
        //`type`                -> "sparkJob"
      )
    ))*/

    logger
  }

  new AppContext(

    stopSparkContext
    , stopImpalaConnection
  )
}