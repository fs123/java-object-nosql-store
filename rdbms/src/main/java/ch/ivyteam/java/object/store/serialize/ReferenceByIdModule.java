package ch.ivyteam.java.object.store.serialize;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

/**
 * Writes and reads virtual ID fields into every type. 
 * Solves stack overflow exceptions on back references without a single annotation
 * 
 * @author rew
 * @since 04.07.2016
 */
public class ReferenceByIdModule extends SimpleModule
{
  private static final PropertyName ID_PROPERTY = new PropertyName("@id");

  private static final ThreadLocal<IdGenerator> ID_GENERATOR = new ThreadLocal<>();
  
  public ReferenceByIdModule()
  {
    setSerializerModifier(new IdWriter());
    setDeserializerModifier(new IdReader());
  }

  public static void setRootTypeHint(Class<?> rootType)
  {
    if (rootType != null)
    {
      ID_GENERATOR.set(new IdGenerator(rootType));
    }
    else
    {
      ID_GENERATOR.set(null);
    }
  }
  
  private static class IdWriter extends BeanSerializerModifier
  {
    @Override
    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription beanDesc,
            BeanSerializerBuilder builder)
    {
      builder.setObjectIdWriter(createObjectIdWriter(config));
      return super.updateBuilder(config, beanDesc, builder);
    }
    
    private ObjectIdWriter createObjectIdWriter(SerializationConfig config)
    {
      JavaType idType = config.constructType(String.class);
      return ObjectIdWriter.construct(idType, ID_PROPERTY, ID_GENERATOR.get(), false);
    }
  }

  private static class IdReader extends BeanDeserializerModifier
  {
    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config, BeanDescription beanDesc,
            BeanDeserializerBuilder builder)
    {
      ObjectIdReader reader = createObjectIdReaderFor(config);
      builder.setObjectIdReader(reader);
      return super.updateBuilder(config, beanDesc, builder);
    }
  
    private ObjectIdReader createObjectIdReaderFor(DeserializationConfig config)
    {
      JavaType idType = config.constructType(String.class);
      JsonDeserializer<?> deser = new IdDeserializer();
      SettableBeanProperty idProp = null;
      SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
      return ObjectIdReader.construct(idType, ID_PROPERTY, ID_GENERATOR.get(), deser, idProp, resolver);
    }
    
    private static class IdDeserializer extends JsonDeserializer<String>
    {
      @Override
      public String deserialize(JsonParser p, DeserializationContext ctxt)
              throws IOException, JsonProcessingException
      {
        return p.readValueAs(String.class);
      }
    }
  }

  private static class IdGenerator extends ObjectIdGenerator<String>
  {
    private final Class<?> scope;
    private int id = 0;
  
    public IdGenerator(Class<?> scope)
    {
      this.scope = scope;
    }
  
    @Override
    public Class<?> getScope()
    {
      return scope;
    }
  
    @Override
    public boolean canUseFor(ObjectIdGenerator<?> gen)
    {
      return true;
    }
  
    @Override
    public ObjectIdGenerator<String> forScope(Class<?> scp)
    {
      return new IdGenerator(scp);
    }
  
    @Override
    public ObjectIdGenerator<String> newForSerialization(Object context)
    {
      return this;
    }
  
    @Override
    public IdKey key(Object key)
    {
      return new IdKey(getClass(), scope, key);
    }
  
    @Override
    public String generateId(Object forPojo)
    {
      return "$" + id++;
    }
  }
}