package ch.ivyteam.java.object.store.serialize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.test.repeat.Repeat;
import ch.ivyteam.test.repeat.RepeatRule;

@RunWith(Parameterized.class)
public class TestSerializationSpeed
{
  @Rule
  public RepeatRule repeatRule = new RepeatRule();

  @Test
  @Repeat(times=50_000)
  public void gugus()
  {
    String json = serializer.serialize(dossier);
    serializer.deserialize(json);
    
    // 10'000 runs
    // Gson    = 3,7|2.2|3.2 sec
    // Jackson = 0.9|0.7|0.6 sec
    // jsonIo  = 4.5|3.5|4.5 sec
    
    // 50'000 runs
    // Gson    = 10,7|13.3|11.6 sec
    // Jackson = 01.8|01.7|02.4 sec
    // jsonIo  = 09.7|15.4|11.7 sec
  }

  @Parameter
  public Serializer<Dossier> serializer;
  
  @Parameter(value=1) public String name;
  @Parameter(value=2) public Dossier dossier;
  
  @Parameters(name = "{index}: serializer - {1}")
  public static Collection<Object[]> data() {
    List<Object[]> d = new ArrayList<>();
    for(Serializer<Dossier> type : getSerializers(Dossier.class))
    {
      d.add(new Object[]{type, type.getClass().getSimpleName(), RandomDossier.generate()});
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
  
  private static class InMemorySerializer<T> implements Serializer<T>{
    Map<Integer, T> fakeStore = new HashMap<>();
    
    private InMemorySerializer(@SuppressWarnings("unused") Class<T> type)
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
