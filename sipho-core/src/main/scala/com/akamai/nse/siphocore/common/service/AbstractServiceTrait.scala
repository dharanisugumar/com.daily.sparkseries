package com.akamai.nse.siphocore.common.service

/** Package for Appcontext
* @author  : Dharani
* @version : 1.0
*/

import com.akamai.nse.siphocore.common.AppContext

/**
 * trait to assist in calling AppContext
 * This trait basically helps in using the appcontext without actually having it as a constructor argument in the class.
 */
trait AbstractServiceTrait {
  lazy val appContext: AppContext = AppContext()
}
