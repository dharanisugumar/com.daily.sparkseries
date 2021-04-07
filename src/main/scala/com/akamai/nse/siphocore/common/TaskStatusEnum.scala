package com.akamai.nse.siphocore.common

/** Package for TaskStatusEnum
 * @author  : Raju Gurumurthy
 * @version : 1.0
 */

object TaskStatusEnum extends Enumeration {

   /**
    * this object is mainly defines the different status levels of logging which helps in application debugging
    */
type TaskStatus = Value
   val STARTED , INPROGRESS , STOPPED , CANCELLED , FAILED , FINISHED , SKIPPED = Value
}
