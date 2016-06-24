package ch.ivyteam.java.object.store.serialize;

import org.junit.Rule;
import org.junit.Test;

import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.test.repeat.Repeat;
import ch.ivyteam.test.repeat.RepeatRule;

public class TestSerializationSpeed extends AbstractSerializerTest
{
  @Rule
  public RepeatRule repeatRule = new RepeatRule();

  private Dossier dossier = RandomDossier.generate();
  
  @Test
  @Repeat(times=50_000)
  public void gugus()
  {
    Serializer<Dossier> serializer = getSerializer(Dossier.class);
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

}
