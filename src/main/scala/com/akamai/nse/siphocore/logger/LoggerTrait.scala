package com.akamai.nse.siphocore.logger

import org.slf4j.LoggerFactory

trait LoggerTrait {


    private[this] val logger = LoggerFactory.getLogger(getClass.getName)


    var eventAttrs: Map[EventAttribute, Any] = Map[EventAttribute,Any]()

    var formatFunctions:Map[EventAttribute,AnyRef => String] = Map[EventAttribute,AnyRef => String]()

    case object eventDescription extends EventAttribute {val name:String = eventDescription.productPrefix}

    case object stageName extends EventAttribute {val name:String = stageName.productPrefix}

    case object stepName extends EventAttribute {val name:String = stepName.productPrefix}

    case object componentService extends EventAttribute {val name:String = componentService.productPrefix}

    case object eventType extends EventAttribute {val name:String = eventType.productPrefix}

    case object eventOutcome extends EventAttribute {val name:String = eventOutcome.productPrefix}

    case object pipelineName extends EventAttribute {val name:String = pipelineName.productPrefix}

    case object pipelineVersion extends EventAttribute {val name:String = pipelineVersion.productPrefix}

    case object eventTimestamp extends EventAttribute {val name:String = eventTimestamp.productPrefix}

    case object startTime extends EventAttribute {val name:String = startTime.productPrefix}

    case object endTime extends EventAttribute {val name:String = endTime.productPrefix}

    case object uniqueRequestId extends EventAttribute {val name:String = uniqueRequestId.productPrefix}

    case object stackTrace extends EventAttribute {val name:String = stackTrace.productPrefix}

    def log (evntLvl: String , stgName: String, stepNm: String, cmpntService: String, evntTyp: String
             , evntOutcm: String, msg: String): Unit = {

        this.addOptions(scala.collection.mutable.Map[EventAttribute, String](
            stageName -> stgName,
            stepName -> stepNm,
            componentService -> cmpntService,
            eventType -> evntTyp,
            eventOutcome -> evntOutcm
        ))
        evntLvl match {
            case  "INFO" => logger.info(msg)
            case  "ERROR" => logger.error(msg)
            case  "DEBUG" => logger.debug(msg)
            case  "WARN"  => logger.warn(msg)
            case  "TRACE" => logger.trace(msg)
        }
    }


    def addOptions(bag: scala.collection.mutable.Map[EventAttribute, String]):Unit ={
        if(bag.nonEmpty){
            for(x<-bag){
                logger.debug(s" event attribute added ${x._1.name} as ${x._2}..")
                this.eventAttrs += (x._1 -> x._2)
            }
        }else{
            logger.error("Map[EventAttribute,String] should not be empty")
            throw new RuntimeException("Error: Empty Map is not accepted")
        }
    }


    def add(item:(EventAttribute,String)): Unit = {
        logger.debug(s"event attributed added ${item._1.name} as ${item._2}.. ")

        if(this.formatFunctions.contains(item._1)){
            lazy val f = this.formatFunctions(item._1)
            val v:String = f(item._2)
            this.eventAttrs +=(item._1 -> v)
        }else{
            this.eventAttrs += item
        }
    }

    def remove(item:EventAttribute):Unit ={
        logger.debug(s"Event attribute removed ${item.name}")
        this.eventAttrs -= item
    }

    def info(msg: String, throwable: Throwable):Unit ={
        logger.info(msg,throwable)
    }

    def info(msg: String):Unit ={
        logger.info(msg)
    }

    def debug(msg: String):Unit ={
        logger.debug(msg)
    }

    def error(msg: String):Unit ={
        logger.error(msg)
    }

    def warn(msg: String):Unit ={
        logger.warn(msg)
    }
}

sealed trait EventAttribute{
    def name: String
    def default:String = ""
    def validate(value:String): Boolean = value!=null && value.length>0
    override def toString: String = name

}
