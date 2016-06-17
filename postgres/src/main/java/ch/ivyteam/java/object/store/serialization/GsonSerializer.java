package ch.ivyteam.java.object.store.serialization;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import ch.ivyteam.serialize.JavaTyping.Elephant;
import ch.ivyteam.serialize.JavaTyping.Labrador;
import ch.ivyteam.serialize.JavaTyping.Lion;

public class GsonSerializer<T> implements Serializer<T> {

	private Class<T> type;
	private Gson gson;
	
	public GsonSerializer(Class<T> type) {
		this.type = type;
		TypeAdapterFactory factory = new TypeAdapterFactory() {
			
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		gson = new GsonBuilder()
				.registerTypeAdapter(Lion.class, new InterfaceAdapter<Lion>())
				.registerTypeAdapter(Labrador.class, new InterfaceAdapter<Labrador>())
				.registerTypeAdapter(Elephant.class, new InterfaceAdapter<Elephant>())
				/*.registerTypeAdapterFactory(factory )*/
				.create();
	}
	
	@Override
	public String serialize(T obj) {
		return gson.toJson(obj);
	}

	@Override
	public T deserialize(String json) {
		return gson.fromJson(json, type);
	}
	
	
	final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	    public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
	        final JsonObject wrapper = new JsonObject();
	        wrapper.addProperty("type", object.getClass().getName());
	        wrapper.add("data", context.serialize(object));
	        return wrapper;
	    }

	    public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
	        final JsonObject wrapper = (JsonObject) elem;
	        final JsonElement typeName = get(wrapper, "type");
	        final JsonElement data = get(wrapper, "data");
	        final Type actualType = typeForName(typeName); 
	        return context.deserialize(data, actualType);
	    }

	    private Type typeForName(final JsonElement typeElem) {
	        try {
	            return Class.forName(typeElem.getAsString());
	        } catch (ClassNotFoundException e) {
	            throw new JsonParseException(e);
	        }
	    }

	    private JsonElement get(final JsonObject wrapper, String memberName) {
	        final JsonElement elem = wrapper.get(memberName);
	        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
	        return elem;
	    }
	}
	
	

}
