package com.akamai.nse.siphocore.common

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}

/** Package for KafkaStringSchema
 * @author  : Dharani
 * @version : 1.0
 */

/**
 * this object helps to takes the kafka input of type string and serialize, deserialize to bytes type.
 */
object KafkaStringSchema extends SerializationSchema[String] with DeserializationSchema[String] {

  import org.apache.flink.api.common.typeinfo.TypeInformation
  import org.apache.flink.api.java.typeutils.TypeExtractor

  override def serialize(t: String): Array[Byte] = t.getBytes("UTF-8")

  override def isEndOfStream(t: String): Boolean = false

  override def deserialize(bytes: Array[Byte]): String = new String(bytes, "UTF-8")

  override def getProducedType: TypeInformation[String] = TypeExtractor.getForClass(classOf[String])
}

