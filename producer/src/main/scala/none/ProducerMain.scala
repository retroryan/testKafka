package none

import java.util.Properties

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

object ProducerMain extends App {

  val config = ConfigFactory.load()

  val events = config.getInt("producer.events")
  val topic = config.getString("producer.topic")
  val brokers = config.getString("producer.host")
  val props = new Properties()

  props.put("bootstrap.servers", brokers)
  props.put("client.id", "producer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)
  val t = System.currentTimeMillis()
  for (nEvents <- Range(0, events)) {
    val key = "messageKey " + nEvents.toString
    val msg = "test message"
    val data = new ProducerRecord[String, String](topic, key, msg)

    //async
    //producer.send(data, (m,e) => {})
    //sync
    producer.send(data)
  }

  System.out.println("sent per second: " + events * 1000 / (System.currentTimeMillis() - t))
  producer.close()
}