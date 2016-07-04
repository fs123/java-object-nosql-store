package ch.ivyteam.java.object.store.serialize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonSerializer<T> implements Serializer<T>
{
  private final Class<T> type;
  private final ObjectMapper mapper;

  public JacksonSerializer(Class<T> type)
  {
    this.type = type;

    mapper = new ObjectMapper();
    mapper.registerModule(new ReferenceByIdModule<>(type));
    
    mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.PROPERTY);
//    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
//    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  }
  
  @Override
  public String serialize(T obj)
  {
    try
    {
      String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      return json;
    }
    catch (JsonProcessingException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public T deserialize(String json)
  {
    try
    {
      return mapper.readValue(json, type);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
