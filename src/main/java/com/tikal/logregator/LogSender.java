package com.tikal.logregator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by ilan on 12/7/16.
 */
public class LogSender extends AbstractVerticle{

    public static String EVENTBUS_ADDRESS = "eventbus.address";
    public static String TOPIC = "elblogs2";

//    public static String KAFKA_IP = "35.156.190.87:9092" ;
    public String KAFKA_IP = "";
//    public static String KAFKA_IP = "localhost:9092" ;


    private Producer producer;
    public static String EVENTBUS_DEFAULT_ADDRESS = "kafka.message.publisher";
    private String busAddress;
    private static final Logger logger = LoggerFactory.getLogger(LogSender.class);

    public static void main(String[] args) {
        new LogSender().sendMessage("a new messsage");
    }

    public LogSender() {
        KAFKA_IP = System.getenv("KAFKA_ADDRESS");
        if (KAFKA_IP == null) {
            KAFKA_IP = "35.156.190.87:9092";
        }
        Properties properties = getKafkaConfig();

        //producer = new KafkaProducer(properties);
        producer = new Producer<String, String>(new ProducerConfig(properties));
    }


//    @Override
//    public void start(final Future<Void> startedResult) {
//        try {
//
//            Properties properties = getKafkaConfig();
//
//            busAddress = EVENTBUS_DEFAULT_ADDRESS;
//
//            producer = new KafkaProducer(properties);
//
//            vertx.eventBus().consumer(busAddress, new Handler<Message<String>>() {
//                @Override
//                public void handle(Message<String> message) {
//                    sendMessage(message.body());
//                }
//            });
//
//            Runtime.getRuntime().addShutdownHook(new Thread() {
//                // try to disconnect from ZK as gracefully as possible
//                public void run() {
//                    shutdown();
//                }
//            });
//
//            startedResult.complete();
//        } catch (Exception ex) {
//            logger.error("Message consumer initialization failed with ex: {}", ex);
//            startedResult.fail(ex);
//        }
//
//    }

    /**
     * Send a message on a pre-configured topic.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        //ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "123", message);
        KeyedMessage<String, String> data = new KeyedMessage<>(TOPIC, KAFKA_IP, message);


        producer.send(data);
    }

    private Properties getKafkaConfig() {

        Properties props = new Properties();
//        props.put("bootstrap.servers", KAFKA_IP);
//        props.put("acks", "all");
//        props.put("retries", 0);
//        props.put("batch.size", 16384);
//        props.put("linger.ms", 1);
//        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("metadata.broker.list", KAFKA_IP);
        //props.put("request.required.acks", "0");
        //props.put("producer.type", "sync") ;
        //props.put("serializer.class", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        //props.put("partitioner.class", "example.producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        return props;
    }


    private void shutdown() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }
}

