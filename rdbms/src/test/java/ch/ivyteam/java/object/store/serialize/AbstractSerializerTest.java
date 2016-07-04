package ch.ivyteam.java.object.store.serialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ch.ivyteam.fintech.Dossier;

@RunWith(Parameterized.class)
public class AbstractSerializerTest
{
  @Parameter
  public Serializer<?> internalSerializer;
  
  @SuppressWarnings("unchecked")
  public <T> Serializer<T> getSerializer(@SuppressWarnings("unused") Class<T> type)
  {
    return (Serializer<T>) internalSerializer;
  }
  
  @Parameter(value=1) public String name;
  
  @Parameters(name = "{index}: serializer - {1}")
  public static Collection<Object[]> data() {
    List<Object[]> d = new ArrayList<>();
    for(Serializer<Dossier> type : getSerializers(Dossier.class))
    {
      d.add(new Object[]{type, type.getClass().getSimpleName()});
    }
    return d;
  }

  private static <T> List<Serializer<T>> getSerializers(Class<T> type)
  {
    return Arrays.asList(
            new InMemorySerializer<>(type), 
            new GsonSerializer<>(type), 
            new JacksonSerializer<>(type), 
            new JsonIOSerializer<>(type));
  }
  
  public static class InMemorySerializer<T> implements Serializer<T>{
    Map<Integer, T> fakeStore = new HashMap<>();
    
    public InMemorySerializer(@SuppressWarnings("unused") Class<T> type)
    {
    }
    
    @Override
    public String serialize(T obj)
    {
      int vmIdentity = System.identityHashCode(obj);
      fakeStore.put(vmIdentity, obj);
      return String.valueOf(vmIdentity);
    }
    @Override
    public T deserialize(String json)
    {
      return fakeStore.get(Integer.valueOf(json));
    }
  }
}
