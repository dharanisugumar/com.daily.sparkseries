package com.akamai.nse.siphocore.connectors.flink.sink

import java.util.{Optional, Properties}

import com.akamai.nse.siphocore.connectors.StreamSinkConnector
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkFixedPartitioner
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

class KafkaSink(bootstrapServers:String,topic:String) extends StreamSinkConnector[String]{

  val producerProperties = new Properties
  producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
  producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
  producerProperties.setProperty(ProducerConfig.ACKS_CONFIG, "all")
  producerProperties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
  producerProperties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5")
  producerProperties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE))
  //high throughput producer, low latency and cpu usage
  producerProperties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024))
  producerProperties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
  producerProperties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20")

  val kafkaProducer = new FlinkKafkaProducer[String](
    topic,
    new KeyedSerializationSchemaWrapper[String](new SimpleStringSchema),
    producerProperties
  )


  override def loader(env: StreamExecutionEnvironment,stream:DataStream[String]): Unit = stream.addSink(kafkaProducer)
}
