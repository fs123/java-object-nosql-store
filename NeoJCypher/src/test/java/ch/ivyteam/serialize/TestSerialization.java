package ch.ivyteam.serialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.ivyteam.java.object.jcypher.CypherDocs;
import ch.ivyteam.merge.TestMerging;
import ch.ivyteam.serialize.JavaTyping.Elephant;
import ch.ivyteam.serialize.JavaTyping.Labrador;
import ch.ivyteam.serialize.JavaTyping.Lion;
import ch.ivyteam.serialize.JavaTyping.Zoo;
import iot.jcypher.domain.DomainAccessFactory;
import iot.jcypher.domain.IDomainAccess;
import iot.jcypher.domain.SyncInfo;

public class TestSerialization
{

  @Test
  public void recursiveReferences()
  {
    CypherDocs<Recursion> docStore = storeOf(Recursion.class);
    Recursion recursion = Recursion.createRecursion();
    docStore.persist(null, recursion);
    
    SyncInfo info = docStore.domain.getSyncInfo(recursion);
    
    Recursion newRecursion = docStore.find(String.valueOf(info.getId()));
    assertThat(newRecursion.key).isEqualTo(recursion.key);
    assertThat(newRecursion.start.key).isEqualTo(recursion.start.key);
    assertThat(newRecursion.start.b.key).isEqualTo(recursion.start.b.key);
    assertThat(newRecursion.start.b.a.b).isEqualTo(recursion.start.b.a.b);
  }
  
  @Test
  public void javaTyping()
  {
    CypherDocs<Zoo> docStore = storeOf(Zoo.class);
    Zoo zoo = JavaTyping.createZoo();
    docStore.persist(null, zoo);
    
    SyncInfo syncInfo = docStore.domain.getSyncInfo(zoo);
    Zoo newZoo = docStore.find(String.valueOf(syncInfo.getId()));
    assertThat(newZoo).isNotNull();
    assertThat(newZoo.a).isInstanceOf(Lion.class);
    assertThat(((Lion)newZoo.a).key).isEqualTo("Simba");
    assertThat(newZoo.b).isInstanceOf(Elephant.class);
    assertThat(((Elephant)newZoo.b).key).isEqualTo("Benjamin");
    assertThat(newZoo.c).isInstanceOf(Labrador.class);
    assertThat(((Labrador)newZoo.c).key).isEqualTo("Lassi");
    
    assertThat(newZoo.d).isEqualTo(zoo.d);
    assertThat(newZoo.e).isEqualTo(zoo.e);
    assertThat(((Labrador)newZoo.e.get("first")).likesToPlayWith)
    .isEqualTo(((Labrador)zoo.e.get("first")).likesToPlayWith);
  }
  
  @Test
  public void sameReference()
  {
    SameReference reference = SameReference.createReferenceTest();
    CypherDocs<SameReference> docStore = storeOf(SameReference.class);
    docStore.persist(null, reference);
    
    SyncInfo syncInfo = docStore.domain.getSyncInfo(reference);
    SameReference newRef = docStore.find(String.valueOf(syncInfo.getId()));
    assertThat(newRef.key).isEqualTo("Start");
    assertThat(newRef.ref1.key).isEqualTo("X");
    assertThat(newRef.ref1).isSameAs(newRef.ref2);
  }
  
  private static <T> CypherDocs<T> storeOf(Class<T> type)
  {
    IDomainAccess domainAccess = DomainAccessFactory.createDomainAccess(TestMerging.realNeo4j(), "SERIALIZATION");
    return new CypherDocs<>(domainAccess, type);
  }
  
}
