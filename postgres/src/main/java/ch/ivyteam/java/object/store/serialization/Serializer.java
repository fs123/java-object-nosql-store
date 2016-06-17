package ch.ivyteam.java.object.store.serialization;

public interface Serializer<T> {
	
	String serialize(T obj);
	T deserialize(String json);

}
