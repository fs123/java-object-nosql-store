package ch.ivyteam.serialize.neo4j;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.GraphDocs;
import ch.ivyteam.neo4j.Neo4jSessionFactory;
import ch.ivyteam.serialize.neo4j.JavaTyping.Elephant;
import ch.ivyteam.serialize.neo4j.JavaTyping.Labrador;
import ch.ivyteam.serialize.neo4j.JavaTyping.Lion;
import ch.ivyteam.serialize.neo4j.JavaTyping.Zoo;

public class TestSerialization
{

  @Test
  public void recursiveReferences()
  {
    Documents<Recursion> docStore = storeOf(Recursion.class);
    Recursion recursion = Recursion.createRecursion();
    docStore.persist(String.valueOf(recursion.getId()), recursion);
    
    Recursion newRecursion = docStore.find(String.valueOf(recursion.getId()));
    assertThat(newRecursion.key).isEqualTo(recursion.key);
    assertThat(newRecursion.start.key).isEqualTo(recursion.start.key);
    assertThat(newRecursion.start.b.key).isEqualTo(recursion.start.b.key);
    assertThat(newRecursion.start.b.a.b).isEqualTo(recursion.start.b.a.b);
  }
  
  @Test
  public void javaTyping()
  {
    Documents<Zoo> docStore = storeOf(Zoo.class);
    Zoo zoo = JavaTyping.createZoo();
    docStore.persist(String.valueOf(zoo.getId()), zoo);
    
    Zoo newZoo = docStore.find(String.valueOf(zoo.getId()));
    assertThat(newZoo).isNotNull();
    assertThat(newZoo.a).isInstanceOf(Lion.class);
    assertThat(((Lion)newZoo.a).key).isEqualTo("Simba");
    assertThat(newZoo.b).isInstanceOf(Elephant.class);
    assertThat(((Elephant)newZoo.b).key).isEqualTo("Benjamin");
    assertThat(newZoo.c).isInstanceOf(Labrador.class);
    assertThat(((Labrador)newZoo.c).key).isEqualTo("Lassi");
  }
  
  @Test
  public void sameReference()
  {
    SameReference reference = SameReference.createReferenceTest();
    Documents<SameReference> docStore = storeOf(SameReference.class);
    docStore.persist(String.valueOf(reference.getId()), reference);
    
    SameReference newRef = docStore.find(String.valueOf(reference.getId()));
    assertThat(newRef.key).isEqualTo("Start");
    assertThat(newRef.ref1.key).isEqualTo("X");
    assertThat(newRef.ref1).isSameAs(newRef.ref2);
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static <T> Documents<T> storeOf(Class<T> type)
  {
    return new GraphDocs(Neo4jSessionFactory.getInstance().getSerializationSession(), type);
  }
  
}
