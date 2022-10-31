package com.codedm.java.kafka.consumer;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Kafka 消费示例
 *
 * @author datatom
 */
public class KafkaConsumerDemo {


    public static void main(String[] args) {

        String krb5 = "/Users/wudongming/Documents/datatom/code/kafka-consumer-demo/src/main/resources/krb5.conf";
        String kafkaLoginConfig = "/Users/wudongming/Documents/datatom/code/kafka-consumer-demo/src/main/resources/kafka_client_jaas.conf";

        System.setProperty("java.security.krb5.conf", krb5);
        System.setProperty("java.security.auth.login.config", kafkaLoginConfig);

        String kafkaServers = "192.168.70.110:6667";
        String groupId = "kafka-consumer-demo2";
        String schemaRegistryUrl = "http://192.168.70.109:8081/";
        List<String> topics = Collections.singletonList("db.hive.test_hive01");

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "false");
        // 配置禁止自动提交，每次从头消费供测试使用
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 使用Confluent实现的KafkaAvroDeserializer
        props.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        // 添加schema服务的地址，用于获取schema
        props.put("schema.registry.url", schemaRegistryUrl);

        // 开启kerberos认证
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");

        KafkaConsumer<String, GenericRecord> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics);

        try {
            while (true) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    GenericRecord user = record.value();
                    System.out.println(user);
//                    consumer.commitSync();
                }
            }
        } finally {
            consumer.close();
        }

    }
}
