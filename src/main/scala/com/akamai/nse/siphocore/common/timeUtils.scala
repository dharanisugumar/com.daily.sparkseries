package com.akamai.nse.siphocore.common

class timeUtils {

  private def getCurrentTimestamp: String = {
    new TimestampConverter().UTCText("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  }
}
