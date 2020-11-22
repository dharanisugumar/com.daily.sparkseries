package com.akamai.nsecore.Connectors.Sink

import com.akamai.nsecore.common.KafkaStringSchema
import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


class KafkaSink(zookeeper:String,groupId:String,bootstrapServer:String) {

  val env = StreamExecutionEnvironment.createLocalEnvironment()

  val kafkaConsumerProperties = Map(
    "zookeeper.connect" -> zookeeper,
    "group.id" -> groupId,
    "bootstrap.servers" -> bootstrapServer
  )

  val kafkaConsumer = new FlinkKafkaConsumer[String](
    "input",
    KafkaStringSchema,
    kafkaConsumerProperties,
    OffsetStore.FLINK_ZOOKEEPER,
    FetcherType.LEGACY_LOW_LEVEL
  )

  val stream = env.addSource(kafkaConsumer)

  env.execute("Kafka Consumer")
}


