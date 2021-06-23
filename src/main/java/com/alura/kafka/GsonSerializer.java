package com.alura.kafka;

import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//� NECESSARIO IMPORTAR A SERIALIZER DO KAFKA
public class GsonSerializer<T> implements Serializer<T> {

	// DEVOLVE UM JSON - SERIALIZADOR (N�O FUNCIONA PARA O KAFKA)
	private Gson gson = new GsonBuilder().create();

	@Override
	public byte[] serialize(String s, T object) {
		return gson.toJson(object).getBytes();
	}

}
