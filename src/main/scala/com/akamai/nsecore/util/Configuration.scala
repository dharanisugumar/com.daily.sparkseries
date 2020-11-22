package com.akamai.nsecore.util

import java.util
import java.util.HashMap

class Configuration {

  /** Stores the concrete key/value pairs of this configuration object. */
  protected var confData: util.HashMap[String, AnyRef] = null

  override def containsKey(key: Any): Boolean = getNode(hash(key), key) != null


}
