package com.akamai.nse.siphocore.processor

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter, Serializable}
import com.akamai.nse.siphocore.common.service.AbstractServiceTrait
import com.akamai.nse.siphocore.common.{TaskStageEnum, TaskStatusEnum}
import com.akamai.nse.siphocore.exception.{SiphoFileException, SiphoKeyNotFoundException}
import com.akamai.nse.siphocore.logger.{EventTypeEnum, LogLevelEnum, LogMessage}
import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.io.Source

/** Package for Processor
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

case class Processor(cache:Map[String,String]) extends Serializable {

  private lazy val keys = cache.keys.toArray

  def analyze(lookupEntry: String): Option[String] = {
    cache.get(lookupEntry)
  }

  def lookup(lookupEntry: String): Option[String] = {
    try {
      keys.find(lookupEntry.endsWith).flatMap(cache.get)
    } catch {
      case e: Exception =>
        throw SiphoKeyNotFoundException(e.getMessage, this.getClass.getName, "lookup")
    }
  }

}

object Processor extends AbstractServiceTrait{

  /* this method :- writeFile - writes the content into the file */

  def writeFile(index:Map[String,String]) = {

    try {
      appContext.jobLogger.eventLog(LogLevelEnum.DEBUG, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Create Configuration file", TaskStatusEnum.FAILED, "", ""))
      val file = new File(index("file.name"))
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, s"Create Configuration file ${index("file.name")}", TaskStatusEnum.FINISHED, "", ""))
      val bw = new BufferedWriter(new FileWriter(file))
      index.foreach(mapValue => {
        if(mapValue._1 =="usage.month.start" || mapValue._1 =="usage.month.end"){
          val line = (s"${mapValue._1}='${mapValue._2}'")
          bw.write(s"${line}\n")
        }else {
          val line = (s"${mapValue._1}=${mapValue._2}")
          bw.write(s"${line}\n")
        }
      })
      bw.close()
    }catch{
      case ex:FileNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.PreProcessor, "Create Configuration file", TaskStatusEnum.FAILED, "", ""))
        SiphoFileException(ex.getMessage,this.getClass().getName,"writeFile","")
    }
  }

  /* this method :- loadFile - reads the file and stores it as a list of key value pairs and throws exception if the input
  * file does not exist  */

  def loadFile(propsFile: String, fileDelimiter: String = "=") = {
    var cache: ListMap[String, String] = ListMap.empty
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.STARTED, "", ""))
    try {
      val file = Source.fromFile(propsFile)
      file.getLines().foreach(x => {
        val kv = x.split(fileDelimiter, 2)
        for {
          k <- kv.headOption
          if k.length > 0
          v <- kv.lift(1)
          if v.length > 0
        } cache += (k.toString -> v.toString)
      })
      file.close()
    }catch{
      case x:FileNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.FAILED, "", ""))
        throw SiphoFileException(x.getMessage,this.getClass().getName,"buildQuery",propsFile)
    }
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.FINISHED, "", ""))
    new Processor(cache)
  }

  def fromFile(propsFile: String, fileDelimiter: String = "="): Int = {
    val getJobId = loadFile(propsFile).lookup("job.id").getOrElse(1)
    getJobId.asInstanceOf[Int]
  }


  /* this method :- getParamProvider - for the corresponding key that we pass it returns the value
  * and stores it in the list. This has been given as a input for jdbc load for flink to read
  * the data in chunks */


  def getParamProvider(processor: Processor,key:String): Array[Array[Serializable]] = {

    val paramList =  processor.lookup(key).get.split(",")
    val queryParameters = new Array[Array[Serializable]](paramList.size)
    var idx = 0
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "passing parameters to JdbcSource", TaskStatusEnum.STARTED, "", ""))
    for (acc <- paramList) {
      queryParameters(idx) = Array[Serializable](acc)
      idx += 1
    }
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "passing parameters to JdbcSource", TaskStatusEnum.FINISHED, "", ""))
    queryParameters
  }

  def buildQuery(fileName: String, processor: Processor, keyValueMap:mutable.Map[String,String]): String = {

    var str = ""
    var outFile = ""
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Creating the stats sql", TaskStatusEnum.STARTED, "", ""))
      val file = Source.fromFile(fileName)
      str = file.mkString
    }catch {
      case ex:FileNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Creating the stats sql", TaskStatusEnum.FAILED, "", ""))
        throw SiphoFileException(ex.getMessage,this.getClass().getName,"buildQuery",fileName)
    }
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Creating the stats sql", TaskStatusEnum.FINISHED, "", ""))
    outFile = keyValueMap.foldLeft(str) {
      case (cur, (from, to)) =>
        try {
          val value = processor.lookup(from).get
          cur.replaceAllLiterally(to, value)
        }catch{
          case x: Exception =>
            appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Get values from Configuration File", TaskStatusEnum.FAILED, "", ""))
            throw SiphoKeyNotFoundException(s" Key Not Found $from",this.getClass.getName,"buildQuery")
        }
    }
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Get values from Configuration File", TaskStatusEnum.FINISHED, "", ""))
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, s"outFile : ${outFile}", TaskStatusEnum.FINISHED, "", ""))
    outFile
  }

  def buildQuery(fileName: String): String = {

    var str = ""
    try {
      appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.STARTED, "", ""))
      val file = Source.fromFile(fileName)
      str = file.mkString
    }catch {
      case ex:FileNotFoundException =>
        appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.FAILED, "", ""))
        throw SiphoFileException(ex.getMessage,this.getClass().getName,"buildQuery",fileName)
    }
    appContext.jobLogger.eventLog(LogLevelEnum.INFO, appContext.job, LogMessage(appContext.logIdentifier, " ", EventTypeEnum.TASK, TaskStageEnum.Transformation, "Reading properties file", TaskStatusEnum.FINISHED, "", ""))
    str
  }
}
