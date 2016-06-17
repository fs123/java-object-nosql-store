package ch.ivyteam.java.object.store.serialization;

import com.google.gson.Gson;

public class GsonSerializer<T> implements Serializer<T> {

	private Class<T> type;
	private Gson gson;
	
	public GsonSerializer(Class<T> type) {
		this.type = type;
		gson = new Gson();
	}
	
	@Override
	public String serialize(T obj) {
		return gson.toJson(obj);
	}

	@Override
	public T deserialize(String json) {
		return gson.fromJson(json, type);
	}

}
