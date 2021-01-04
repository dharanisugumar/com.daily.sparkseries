package com.akamai.nse.siphocore.logger

class TaskLogger extends LoggerTrait {
  override def log(evntLvl: String, stgName: String, stepNm: String, cmpntService: String, evntTyp: String, evntOutcm: String, msg: String): Unit = super.log(evntLvl, stgName, stepNm, cmpntService, evntTyp, evntOutcm, msg)
}
