package net.tianzx.kafka;

import com.google.common.collect.Lists;
import net.tianzx.utils.Utils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: tianzx
 * Date: 2018-12-03  14:35
 * Email: zixuan.tian@nio.com
 */
public class MyKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);

    public void init(Properties props) throws IOException {

        props.put("bootstrap.servers", "10.125.243.61:9092,10.125.242.97:9092,10.125.252.75:9092");
//        props.put("acks", "1");
        props.put("buffer.memory", 33_554_432);
        props.put("batch.size", 16_384);
//        props.put("max.request.size", 10_485_760);
//        props.put("client.id", "msg_api_" + 1);
        props.put("retries", 3);
        props.put("linger.ms", 1);
//        props.put("retry.backoff.ms", 3000);
//        props.put("max.block.ms", 60000);
        props.put("request.timeout.ms", 30000);
        props.put("key.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");

        String saslConfigFormat = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        String saslUserName = Utils.getConfig("saslUserName");
        String saslPassword = Utils.getConfig("saslPassword");
        String saslConfig = String.format(saslConfigFormat, saslUserName, saslPassword);
        props.put("sasl.jaas.config", saslConfig);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();
        MyKafkaProducer myKafkaProducer = new MyKafkaProducer();
        myKafkaProducer.init(props);
        Producer<String, String> producer = new KafkaProducer(props);
        Producer<String, String> producer2 = new KafkaProducer(props);
        String topicName = "swc-cvs-nmp-mac-10001-notification";
        byte[] value = Utils.readFile("test.file");
        int msgNum = 1_00;
        List list = Lists.newArrayList();
        long start = System.currentTimeMillis();
        AtomicLong al = new AtomicLong(0);
        int num = 0;
        long startTime = System.currentTimeMillis();
        while (true) {

            for (int i = 0; i < msgNum; i++) {
                String nonce = i + "";
                ProducerRecord pr = new ProducerRecord(topicName, null, startTime, nonce.getBytes(), value);
                Future<RecordMetadata> future = producer.send(pr, (recordMetadata, e) -> {
                    LOGGER.info(String.valueOf(al.incrementAndGet()));
                    if (e != null) {
                        LOGGER.debug("send2kafka: fail to send msgSize {} of topic {} to kafka {}, ",
                                recordMetadata.serializedValueSize(), recordMetadata.topic(), e);
                    } else {
                        producer2.send(pr, (recordMetadata2, e2) -> {
                            LOGGER.info(String.valueOf(al.getAndAdd(1)));
                            if (e2 != null) {
                                LOGGER.debug("send2kafka: fail to send msgSize {} of topic {} to kafka {}, ",
                                        recordMetadata.serializedValueSize(), recordMetadata.topic(), e);
                            } else {
                                LOGGER.info("send2kafka: success");
                            }
                        });
                    }
                });
            }
        }

    }
}
