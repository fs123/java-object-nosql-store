package ch.ivyteam.java.object.store.serialize;

public interface Serializer<T>
{
  String serialize(T obj);
  T deserialize(String json);
}
