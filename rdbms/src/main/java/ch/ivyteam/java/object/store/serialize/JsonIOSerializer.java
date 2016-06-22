package ch.ivyteam.java.object.store.serialize;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

public class JsonIOSerializer<T> implements Serializer<T>
{
  public JsonIOSerializer(@SuppressWarnings("unused") Class<T> type)
  {
  }

  @Override
  public String serialize(T obj)
  {
    return JsonWriter.objectToJson(obj);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T deserialize(String json)
  {
    return (T) JsonReader.jsonToJava(json);
  }

}
