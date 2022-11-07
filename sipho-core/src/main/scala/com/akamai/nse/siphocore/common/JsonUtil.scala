package com.akamai.nse.siphocore.common

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/** Package for JsonUtil
 * @author  : Dharani
 * @version : 1.0
 */

/**
 * this object mainly converts the given value to json format
 */
object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setSerializationInclusion(Include.NON_NULL)

  /**
   * this method takes input of type Map[k,v] and returns in the json format
   */
  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  /**
   * this method takes input of any type and returns in the json format
   */
  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }
  /**
   * this method takes input json and returns in the Map[k,v] format
   */

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  /**
   * this method takes input json and returns in any specified format
   */
  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json)
  }

}
