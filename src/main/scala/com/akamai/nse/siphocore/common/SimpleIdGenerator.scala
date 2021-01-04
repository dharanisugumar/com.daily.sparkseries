package com.akamai.nse.siphocore.common

/**
 *
 */

import java.util.UUID

class SimpleIdGenerator extends IdGeneratorTrait{

  /**
   *
   * @param prefix prefix attached to the unique identifier
   * @return unique identifier for any object
   */
  override def generator(prefix:String): String = {
    val id = 'l'+UUID.randomUUID().toString
    id
  }

}
