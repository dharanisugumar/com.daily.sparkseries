package com.akamai.nse.siphocore.common

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.TimeZone

/** Package for TimestampConverter
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

class TimestampConverter (val ts: Timestamp = Timestamp.from(Instant.now())) {
  val millis: Long = ts.getTime
  def UTCText(format: String): String =       TimeZone.getTimeZone("UTC").toString
  def localText(format: String): String = ???
  def getMinus1Sec: TimestampConverter = new TimestampConverter(Timestamp.from(Instant.ofEpochMilli(millis-1000)))
}
/**
 * this is basically useful in timestamp conversions which has made generic and can be used across all the applications
 */


  object TimestampConverter {
    final val TIMESTAMP_MINUS_INFINITY = parse("1800-01-01T00:00:00.0+00:00", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

    final val TIMESTAMP_PLUS_INFINITY = parse("2999-01-01T00:00:00.0+00:00", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")


    final def parse(inputString: String, inputDateFormat: String): TimestampConverter = {
      val simpleDateFormat = new SimpleDateFormat(inputDateFormat)
      simpleDateFormat.setLenient(false)
      new TimestampConverter(Timestamp.from(Instant.ofEpochMilli(simpleDateFormat.parse(inputString).getTime)))
    }
  }
