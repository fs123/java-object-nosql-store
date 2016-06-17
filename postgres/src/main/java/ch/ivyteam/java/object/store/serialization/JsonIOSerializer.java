package ch.ivyteam.java.object.store.serialization;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

public class JsonIOSerializer<T> implements Serializer<T> {

	private Class<T> type;

	public JsonIOSerializer(Class<T> type) {
		this.type = type;
	}
	
	@Override
	public String serialize(T obj) {
		return JsonWriter.objectToJson(obj);
	}

	@Override
	public T deserialize(String json) {
		return (T) JsonReader.jsonToJava(json);
	}

}
