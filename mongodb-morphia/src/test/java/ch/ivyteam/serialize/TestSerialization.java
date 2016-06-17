package ch.ivyteam.serialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import ch.ivyteam.serialize.JavaTyping.Elephant;
import ch.ivyteam.serialize.JavaTyping.Labrador;
import ch.ivyteam.serialize.JavaTyping.Lion;
import ch.ivyteam.serialize.JavaTyping.Zoo;

public class TestSerialization
{
	
	private static Datastore datastore;
	
  @BeforeClass
  public static void init() throws InterruptedException {
    final Morphia morphia = new Morphia();
    datastore = morphia.createDatastore(new MongoClient(), "fintech");
    datastore.ensureIndexes();
  	datastore.getDB().dropDatabase();
  }
  /**
   * Fails
   * <pre> 
   * java.lang.StackOverflowError
	 *	at org.mongodb.morphia.mapping.MappedClass.callGlobalInterceptors(MappedClass.java:456)
	 *	at org.mongodb.morphia.mapping.MappedClass.callLifecycleMethods(MappedClass.java:427)
	 *	at org.mongodb.morphia.mapping.Mapper.toDBObject(Mapper.java:571)
   */
  @Test
  public void recursiveReferences()
  {
    Recursion recursion = Recursion.createRecursion();
    datastore.save(recursion);
    
    Recursion newRecursion = datastore.find(Recursion.class, "key", "Start").get();
    assertThat(newRecursion.key).isEqualTo(recursion.key);
    assertThat(newRecursion.start.key).isEqualTo(recursion.start.key);
    assertThat(newRecursion.start.b.key).isEqualTo(recursion.start.b.key);
    assertThat(newRecursion.start.b.a.b).isEqualTo(recursion.start.b.a.b);
  }
  
  /**
   * Stored format:
   * <pre>
   * {
   *   "_id":ObjectId("5762b23de6090a0fc4933249"),
   *   "className":"ch.ivyteam.serialize.JavaTyping$Zoo",
   *   "key":"Tierig",
   *   "a":{  
   *     "className":"ch.ivyteam.serialize.JavaTyping$Lion",
   *     "key":"Simba"
   *   },
   *   "b":{  
   *     "className":"ch.ivyteam.serialize.JavaTyping$Elephant",
   *     "key":"Benjamin"
   *   },
   *   "c":{  
   *     "className":"ch.ivyteam.serialize.JavaTyping$Labrador",
   *     "key":"Lassi"
   *   }
   * }
   */
  @Test
  public void javaTyping()
  {
    Zoo zoo = JavaTyping.createZoo();
    zoo.key = "Tierig";
    datastore.save(zoo);
    
    Zoo newZoo = datastore.find(Zoo.class, "key", "Tierig").get();
    
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
  }
  
  /**
   * Fails:
   * AssertionError -> its NOT the same object
   */
  @Test
  public void sameReference()
  {
    SameReference reference = SameReference.createReferenceTest();
    datastore.save(reference);

    SameReference newRef = datastore.find(SameReference.class, "key", "Start").get();
    assertThat(newRef.key).isEqualTo("Start");
    assertThat(newRef.ref1.key).isEqualTo("X");
    assertThat(newRef.ref1).isSameAs(newRef.ref2);
  }
}
