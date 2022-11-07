package com.akamai.nse.siphocore.logger

import com.akamai.nse.siphocore.logger.EventTypeEnum.EventType
import com.akamai.nse.siphocore.common.TaskStageEnum.TaskStage
import com.akamai.nse.siphocore.common.TaskStatusEnum.TaskStatus

/** Package for LogMessage
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

case class LogMessage (eventId:String,eventDescription:String ,eventType:EventType ,stageName:TaskStage ,
                       stepName:String ,status:TaskStatus,eventOutPut:String,message:String )
