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
	 * Caso tenha mais de um servi�o dentro de um mesmo grupo, � necess�rio
	 * verificar as parti��es do t�pico, caso tenha apenas 1, somente um consumidor
	 * recebera as mensagens, e caso dois consumidores partilhem da mesma parti��o,
	 * ambos ir�o processar as mesmas informa��es.
	 * 
	 * 
	 * Para alterar as parti��es � necess�rio aumentar o n�mero de parti��es para novos topicos 
	 * dentro do arquivo "Config/Server.properties"
	 * 
	 * 
	 * E para alterar de um t�pico j� existente o comando seria:
	 * ./bin/windows/kafka-topics.bat --alter --zookeeper localhost:2181 --topic NOME_DO_TOPICO 
	 * --partions NUMERO_DE_PARTI��ES
	 * 
	 * 
	 * CASO UM �NICO SERVICE DE UM DETERMINADO GRUPO ESTEJA EXECUTANDO, ELE TOMAR� CONTA DE TODAS AS PARTI��ES DO 
	 * T�PICO
	 * E CASO SEJA EXECUTADOS MAIS SERVICES � FEITA A PARALELIZA��O/DISTRIBUIDO ENTRE OS MESMOS 
	 * 
	 * **# N�MERO DE PARTI��ES TEM QUE SER >= N�MERO DE CONSUMIDORES DE UM GRUPO
	 * 
	 * PARA O KAFKA DIRECIONAR ENTRE AS PARTI��ES DEPENDE DA HASH DA CHAVE/KEY
	 * 
	 */ 

}
