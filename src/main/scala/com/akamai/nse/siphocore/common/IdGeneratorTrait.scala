package com.akamai.nse.siphocore.common

/** Package for IdGenerator
 * @author  : Dharani
 * @version : 1.0
 */

/**
 * trait  to generate a job id for logging
 */
trait IdGeneratorTrait {

  def generator(prefix:String):String
}
