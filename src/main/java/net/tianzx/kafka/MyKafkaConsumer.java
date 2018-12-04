package net.tianzx.kafka;

import com.google.common.collect.Lists;
import net.tianzx.utils.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Author: tianzx
 * Date: 2018-12-03  16:39
 * Email: zixuan.tian@nio.com
 */
public class MyKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKafkaConsumer.class);

    public void init(Properties props) throws IOException {
        props.put("bootstrap.servers", "10.125.243.61:9092,10.125.242.97:9092,10.125.252.75:9092");
        props.put("acks", "1");
        props.put("group.id", "kafka.consumer.group.id" + "msg_kafka_consumer_" + 1);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "60000");
        props.put("max.poll.records", "500");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");

        String saslConfigFormat = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        String saslUserName = Utils.getConfig("saslUserName");
        String saslPassword = Utils.getConfig("saslPassword");
        String saslConfig = String.format(saslConfigFormat, saslUserName, saslPassword);
        props.put("sasl.jaas.config", saslConfig);

    }

    public static void main(String[] args) throws IOException {
        MyKafkaConsumer consumer = new MyKafkaConsumer();
        Properties properties = new Properties();
        String topics = "swc-cvs-nmp-mac-10001-notification";
        List list = Lists.newArrayList();
        list.add(topics);
        consumer.init(properties);
        consumer.consume(properties, list);

    }

    public void consume(Properties props, List topics) {
//        long sendStartTime = System.currentTimeMillis();
        KafkaConsumer kafkaConsumer = new KafkaConsumer(props);
        kafkaConsumer.subscribe(topics);
        while (true) {
            ConsumerRecords<byte[], byte[]> records = kafkaConsumer.poll(1_000);
//            records.iterator().next().value();
            records.forEach(record -> {
                System.err.println("receive data :" + new String(record.value()));
//                LOGGER.info("receive data : {}" + record.value());
            });
//            System.out.println(1);
        }
    }
}
