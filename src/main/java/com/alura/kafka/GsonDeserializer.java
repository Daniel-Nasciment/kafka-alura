package com.alura.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonDeserializer<T> implements Deserializer<T> {

	public static String TYPE_CONFIG = "br.com.alura.ecommerce.type_config";
	private Gson gson = new GsonBuilder().create();
	private Class<T> type;

	
	// AQUI É RECEBIDO AS CONFIGURAÇÕES DO KAFKA
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// AQUI DEVOLVE O NOME DO TIPO
		// VALUEOF - PARA EVITAR O VALOR NULL (SE FOR NULL DEVOLVE NULL)
		String typeName = String.valueOf(configs.get(TYPE_CONFIG));
		try {
			
			// FORNAME É - MEU TYPE
			// É NECESSÁRIO COLOCAR TRY CATCH POIS PODE SER PASSADO POR ENGANO UMA CLASSE QUE N EXISTA
			this.type = (Class<T>) Class.forName(typeName);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public T deserialize(String s, byte[] bytes) {
		//PRECISA SABER PARA QUAL TIPO QUE SERA DESERIALIZADO
		//PASSAR O TIPO PARA O QUAL SERA DESERIALIZADO
		return gson.fromJson(new String(bytes), type);
	}

}
