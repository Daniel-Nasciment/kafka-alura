package com.alura.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());
		String value = "3824893748,3234234,324234";
		// AP�S O NOME DO T�PICO VEM A CHAVE/HASH
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", value, value);
		String email = "Email sendo enviado agora...";
		ProducerRecord<String, String> emailRecord = new ProducerRecord<String, String>("ECOMMERCE_NEW_EMAIL", email,
				email);

		Callback callback = (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println("Sucesso enviado " + data.topic() + ":::partition " + data.partition() + "| offser "
					+ data.offset() + "/ timestamp " + data.timestamp());
		};

		producer.send(record, callback).get();

		producer.send(emailRecord, callback).get();
	}

	private static Properties properties() {

		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		// TRANSFORMADORES/SERIALIZADORES DE STRINGS PARA BYTES
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		return properties;
	}
	
	/*
	 * NO REBALANCEAMENTO AS MENSAGENS N�O S�O COMMITADAS
	 * NO CASO DO POLL � FEITO O COMMIT A DETERMINADOS "TANTOS DE MENSAGENS"
	 * COM A CONFIGURA��O DO CONSUMMER "MAX_POLL" DE 1 EM 1 � FEITO O COMMIT
	 * DESSA FORMA ELIMINAMOS UM POSSIVEL PROBLEMA DE NECESSITAR O PROCESSAMENTO DA MESMA MENSAGEM
	 * MAIS DE UMA VEZ  
	 */ 

}
