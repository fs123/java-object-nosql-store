package ch.ivyteam.java.object.store.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonSerializer<T> implements Serializer<T> {

	private Class<T> type;
	private ObjectMapper mapper = new ObjectMapper();

	public JacksonSerializer(Class<T> type) {
		this.type = type;
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	@Override
	public String serialize(T obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public T deserialize(String json) {
		try {
			return mapper.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
