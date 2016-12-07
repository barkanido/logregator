package com.tikal.logregator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by ilan on 12/7/16.
 */
public class LogSender extends AbstractVerticle{

    public static String EVENTBUS_ADDRESS = "eventbus.address";
    public static String TOPIC = "elblogs";

    public static String KAFKA_IP = "35.158.66.211:9092" ;
//    public static String KAFKA_IP = "localhost:9092" ;


    private Producer producer;
    public static String EVENTBUS_DEFAULT_ADDRESS = "kafka.message.publisher";
    private String busAddress;
    private static final Logger logger = LoggerFactory.getLogger(LogSender.class);

    public static void main(String[] args) {
        new LogSender().sendMessage("a message");
    }

    public LogSender() {
        Properties properties = getKafkaConfig();

        producer = new KafkaProducer(properties);
    }


    @Override
    public void start(final Future<Void> startedResult) {
        try {

            Properties properties = getKafkaConfig();

            busAddress = EVENTBUS_DEFAULT_ADDRESS;

            producer = new KafkaProducer(properties);

            vertx.eventBus().consumer(busAddress, new Handler<Message<String>>() {
                @Override
                public void handle(Message<String> message) {
                    sendMessage(message.body());
                }
            });

            Runtime.getRuntime().addShutdownHook(new Thread() {
                // try to disconnect from ZK as gracefully as possible
                public void run() {
                    shutdown();
                }
            });

            startedResult.complete();
        } catch (Exception ex) {
            logger.error("Message consumer initialization failed with ex: {}", ex);
            startedResult.fail(ex);
        }

    }

    /**
     * Send a message on a pre-configured topic.
     *
     * @param message the message to send
     */
    public void sendMessage(String message) {
        ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, System.currentTimeMillis(), message);

        producer.send(record);
    }

    private Properties getKafkaConfig() {

        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_IP);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }


    private void shutdown() {
        if (producer != null) {
            producer.close();
            producer = null;
        }
    }
}

