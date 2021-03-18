package com.akamai.nse.siphocore.connectors.flink.source

import java.util.Properties
import com.akamai.nse.siphocore.common.KafkaStringSchema
import com.akamai.nse.siphocore.connectors.StreamSourceConnector
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.api.scala._

/** Package for FileSink
 * @author  : Dharani Sugumar
 * @version : 1.0
 */

class KafkaSource(zookeeper:String,groupId:String,bootstrapServer:String,topic:String) extends StreamSourceConnector[String]{

  val properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("zookeeper.connect", "localhost:2181")
  properties.setProperty("group.id", "test")

  val kafkaConsumer = new FlinkKafkaConsumer[String](
    topic,
    KafkaStringSchema,
    properties
  )

  /* this method fetches the data from the Kafka topic
*/

  override def generate(env: StreamExecutionEnvironment): DataStream[String] =  {

    val ds = env.addSource(kafkaConsumer)
    ds
  }

}


