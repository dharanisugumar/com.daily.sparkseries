package com.akamai.nse.siphocore.model

case class ProcedureInfo(
                         proc_name: String,
                         db_name: String,
                         inputs: List[String],
                         outputs: List[String]
                       )
