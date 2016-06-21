package ch.ivyteam.serialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.postgres.JdbcDocumentPersistency;
import ch.ivyteam.java.object.store.serialization.GsonSerializer;
import ch.ivyteam.java.object.store.serialization.JacksonSerializer;
import ch.ivyteam.java.object.store.serialization.JsonIOSerializer;
import ch.ivyteam.serialize.JavaTyping.Elephant;
import ch.ivyteam.serialize.JavaTyping.Labrador;
import ch.ivyteam.serialize.JavaTyping.Lion;
import ch.ivyteam.serialize.JavaTyping.Zoo;
import ch.ivyteam.test.postgres.PostgresCon;

//These Tests don't yet work
public class TestSerialization
{

  @Test
  public void recursiveReferences()
  {
    Documents<Recursion> docStore = storeOf(Recursion.class);
    
    
    Recursion recursion = Recursion.createRecursion();
    String key = "123";
    docStore.persist(key, recursion);
    
    Recursion newRecursion = docStore.find("123");
    assertThat(newRecursion.key).isEqualTo(recursion.key);
    assertThat(newRecursion.start.key).isEqualTo(recursion.start.key);
    assertThat(newRecursion.start.b.key).isEqualTo(recursion.start.b.key);
    assertThat(newRecursion.start.b.a.b).isEqualToComparingFieldByFieldRecursively(recursion.start.b.a.b);
    assertThat(newRecursion.start.b).isSameAs(newRecursion.start.b.a.b);
  }
  
  @Test
  public void javaTyping()
  {
    Documents<Zoo> docStore = storeOf(Zoo.class);
    Zoo zoo = JavaTyping.createZoo();
    String key ="456";
    docStore.persist(key, zoo);
    
    Zoo newZoo = docStore.find(key);
    assertThat(newZoo).isNotNull();
    assertThat(newZoo.a).isInstanceOf(Lion.class);
    assertThat(((Lion)newZoo.a).key).isEqualTo("Simba");
    assertThat(newZoo.b).isInstanceOf(Elephant.class);
    assertThat(((Elephant)newZoo.b).key).isEqualTo("Benjamin");
    assertThat(newZoo.c).isInstanceOf(Labrador.class);
    assertThat(((Labrador)newZoo.c).key).isEqualTo("Lassi");
    
    assertThat(newZoo.d).containsExactlyElementsOf(zoo.d);
    assertThat(newZoo.e.entrySet()).containsExactlyElementsOf(zoo.e.entrySet());
    assertThat(((Labrador)newZoo.e.get("first")).likesToPlayWith)
  	.isEqualTo(((Labrador)zoo.e.get("first")).likesToPlayWith);
    
    assertThat(newZoo.e.get("first")).isNotSameAs(zoo.e.get("first"));
  }
  
  @Test
  public void sameReference()
  {
    SameReference reference = SameReference.createReferenceTest();
    Documents<SameReference> docStore = storeOf(SameReference.class);
    String key = "789";
    docStore.persist(key, reference);
    
    SameReference newRef = docStore.find(key);
    assertThat(newRef.key).isEqualTo("Start");
    assertThat(newRef.ref1.key).isEqualTo("X");
    assertThat(newRef.ref1).isSameAs(newRef.ref2);
  }
  
  private static <T> Documents<T> storeOf(Class<T> type)
  {
	JdbcDocumentPersistency persistency = new JdbcDocumentPersistency();
	return persistency.get(type, PostgresCon.connectionString, new JsonIOSerializer<T>(type));
  }
  

  
}
