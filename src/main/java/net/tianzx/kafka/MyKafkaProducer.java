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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Author: tianzx
 * Date: 2018-12-03  14:35
 * Email: zixuan.tian@nio.com
 */
public class MyKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaProducer.class);

    public void init(Properties props) throws IOException {

        props.put("bootstrap.servers", "10.125.243.61:9092,10.125.242.97:9092,10.125.252.75:9092");
        props.put("acks", "1");
        props.put("buffer.memory", 33_554_432);
        props.put("batch.size", 1_048_576);
        props.put("max.request.size", 10_485_760);
        props.put("client.id", "msg_api_" + 1);
        props.put("retries", 30);
        props.put("linger.ms", 100);
        props.put("retry.backoff.ms", 3000);
        props.put("request.timeout.ms", 10_000);
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
        String topicName = "swc-cvs-nmp-mac-10001-notification";
        String value = "test";
        int msgNum = 10;

        List list = Lists.newArrayList();
        for (int i = 0; i < msgNum; i++) {
            long startTime = System.currentTimeMillis();
            String nonce = i + "";
            ProducerRecord pr = new ProducerRecord(topicName, null, startTime, nonce.getBytes(), value.getBytes());
            LOGGER.info("record producer send time {}", startTime);
            Future<RecordMetadata> future = producer.send(pr, (recordMetadata, e) -> {
                LOGGER.info(String.valueOf(recordMetadata.partition()));
                long midTime = recordMetadata.timestamp();
                long endTime = System.currentTimeMillis();
                LOGGER.info("record kafka arrive time {}", midTime);
                LOGGER.info("record receive time {}", endTime - startTime);
                if (e != null) {
                    LOGGER.debug("send2kafka: fail to send msgSize {} of topic {} to kafka {}, ",
                            recordMetadata.serializedValueSize(), recordMetadata.topic(), e);
                } else {
                    LOGGER.info("fail" + e.getMessage());
                    LOGGER.debug("send2kafka: send msg {} of clientId {} to kafka {} success");
                }
            });
            list.add(future);
        }
        list.forEach(result -> {
            Future<RecordMetadata> future = (Future<RecordMetadata>) result;
            try {
//                System.err.println(future.get());
                LOGGER.info("send success :{}", ((Future<RecordMetadata>) result).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        while (true) {
            long startTime = System.currentTimeMillis();
            Thread.sleep(1000);
            long endTime = System.currentTimeMillis();
            LOGGER.info(String.valueOf(endTime - startTime));

        }
    }
}
