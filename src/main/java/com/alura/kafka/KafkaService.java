package com.alura.kafka;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

//DESERIALIZAR DE STRING PARA O TIPO "T"
class KafkaService<T> implements Closeable {
	private final KafkaConsumer<String, T> consumer;
	private final ConsumerFunction parse;

	KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> propriedadesExtras) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties(type, groupId ,propriedadesExtras));
        consumer.subscribe(Collections.singletonList(topic));
    }

	KafkaService(String groupId, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> propriedadesExtras) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<>(properties(type, groupId, propriedadesExtras));
		consumer.subscribe(topic);
	}

	void run() {
		while (true) {
			ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("Encontrei " + records.count() + " registros");
				for (ConsumerRecord<String, T> record : records) {
					parse.consume(record);
				}
			}
		}
	}

	private Properties properties(Class<T> type, String groupId, Map<String, String> propriedadesExtras) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		//CRIAÇÃO DO DESERIALIZER
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		//PROPRIEDADE CRIADA
		properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
		properties.putAll(propriedadesExtras);
		return properties;
	}

	@Override
	public void close() {
		consumer.close();
	}
}
