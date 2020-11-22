package com.akamai.nsecore.Connectors.Source

import java.util.{Optional, Properties}

import com.akamai.nsecore.common.KafkaStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.{Logger, LoggerFactory}

class KafkaSource(bootstrapServers:String,topic:String,retries:String,acks:String,idempotence:String) {

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
    KafkaStringSchema,
    producerProperties,
    FlinkKafkaProducer.Semantic.EXACTLY_ONCE)


}
