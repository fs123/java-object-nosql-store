package ch.ivyteam.java.object.store.serialize;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

public class JacksonSerializer<T> implements Serializer<T> {

	private Class<T> type;
	private ObjectMapper mapper = new ObjectMapper();

	public JacksonSerializer(Class<T> type) {
		this.type = type;
		IdGenerator idGenerator = new IdGenerator(type);
	          SerializerFactory factory = mapper.getSerializerFactory().withSerializerModifier(new SerializerModifier(idGenerator));
	          
	         // ObjectIdReader.construct(idType, propName, generator, deser, idProp, resolver) // register me!!!!!
	          
	          
	          
	          
	          mapper.setSerializerFactory(factory);
//	          objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
//	          JsonNode result = mapper.valueToTree(value);

		mapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, true);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	  private static class SerializerModifier extends BeanSerializerModifier
	  {
	    private ObjectIdGenerator<?> idGenerator;

      public SerializerModifier(ObjectIdGenerator<?> generator)
            {
              this.idGenerator = generator;
            }
	    
	    @Override
	    public BeanSerializerBuilder updateBuilder(SerializationConfig config, BeanDescription beanDesc,
	            BeanSerializerBuilder builder)
	    {      
	      JavaType idType = config.getTypeFactory().constructSimpleType(String.class, new JavaType[0]);
	      builder.setObjectIdWriter(ObjectIdWriter.construct(idType, new PropertyName("@"+"id"), idGenerator, false));
	      
	      return super.updateBuilder(config, beanDesc, builder);
	    }
	    
	    
	  }
	  
	  private static class IdGenerator extends ObjectIdGenerator<String>
	  {
	    private Class<?> scope;
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
//	      return (gen.getClass() == getClass()) && (gen.getScope() == scope);
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
	      return "$"+id ++;
	    }
	    
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
