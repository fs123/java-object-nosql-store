package ch.ivyteam.java.object.store.serialize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonSerializer<T> implements Serializer<T>
{
  private final Class<T> type;
  private static ObjectMapper mapper;

  public JacksonSerializer(Class<T> type)
  {
    this.type = type;
    
    if (mapper == null)
    {
      mapper = createMapper();
    }
  }

  private static ObjectMapper createMapper()
  {
    ObjectMapper jackson = new ObjectMapper();
    jackson.registerModule(new ReferenceByIdModule());
    jackson.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.PROPERTY);
    jackson.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    jackson.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
    jackson.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return jackson;
  }
  
  @Override
  public String serialize(T obj)
  {
    try
    {
      ReferenceByIdModule.setRootTypeHint(type);
      String json = mapper.writerFor(type).writeValueAsString(obj);
      return json;
    }
    catch (JsonProcessingException ex)
    {
      throw new RuntimeException(ex);
    }
    finally
    {
      ReferenceByIdModule.setRootTypeHint(null);
    }
  }

  @Override
  public T deserialize(String json)
  {
    try
    {
      ReferenceByIdModule.setRootTypeHint(type);
      return mapper.readerFor(type).readValue(json);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      ReferenceByIdModule.setRootTypeHint(null);
    }
  }
  
}
