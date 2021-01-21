package com.akamai.nse.siphocore.common

object TaskStatusEnum extends Enumeration {
type TaskStatus = Value
   val STARTED , INPROGRESS , STOPPED , CANCELLED , FAILED , FINISHED = Value
}
