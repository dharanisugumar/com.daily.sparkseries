package com.akamai.nse.siphocore.common

/** Package for TaskStageEnum
 * @author  : Raju Gurumurthy
 * @version : 1.0
 */
/**
 * this object is mainly used for logging of different modules
 */
object TaskStageEnum extends Enumeration {
type TaskStage = Value
   val PreProcessor , Transformation , PostProcessor = Value
}
