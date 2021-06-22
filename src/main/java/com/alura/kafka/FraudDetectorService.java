package com.alura.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class FraudDetectorService {

	public static void main(String[] args) {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties());
		// O CONSUMIDOR CONSOME MENSAGENS DE ALGUM TÓPICO
		// SINGLETON É UMA FORMA "FÁCIL DE SE CRIAR UMA LISTA"
		consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));
		while (true) {
			// .POOL VERIFICA SE TEM MENSAGENS DURANTE UM CERTO PERIODO DE TEMPO
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("Encontrei " + records.count() + " registros");
				for (ConsumerRecord<String, String> record : records) {
					System.out.println("----------------------------------------");
					System.out.println("Processing NEW ORDER, checking for fraud");
					System.out.println(record.key());
					System.out.println(record.value());
					try {
						// NESSE CASO SÓ ESTÁ SIMULANDO UMA DEMORA
						Thread.sleep(5000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("Order processsed");
				}
			}
		}
	}

	private static Properties properties() {
		Properties properties = new Properties();

		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		// COMO O PRODUTOR SERIALIZA DE STRING PARA BYTE, O CONSUMIDOR PRECISA
		// DESSERIALIZAR DE BYTE PARA STRING
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// 1 A 1 VAI SENDO FEITO O COMMIT || O POLL TEM UM COMMIT "EMBUTIDO"
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		// RECEBERÁ TODAS AS MENSAGENS
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());

		return properties;
	}

}
