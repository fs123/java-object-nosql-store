package ch.ivyteam.serialize;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ch.ivyteam.java.object.store.MemoryJsonStore;
import ch.ivyteam.java.object.store.ObjectStore;
import ch.ivyteam.java.object.store.serialize.AbstractSerializerTest;
import ch.ivyteam.java.object.store.serialize.Serializer;
import ch.ivyteam.serialize.JavaTyping.Animal;
import ch.ivyteam.serialize.JavaTyping.Elephant;
import ch.ivyteam.serialize.JavaTyping.Labrador;
import ch.ivyteam.serialize.JavaTyping.Lion;
import ch.ivyteam.serialize.JavaTyping.Zoo;

public class TestSerialization extends AbstractSerializerTest
{

  @Test
  public void recursiveReferences()
  {
    ObjectStore<Recursion> docStore = storeOf(Recursion.class);
    Recursion recursion = Recursion.createRecursion();
    Long key = 123l;
    docStore.merge(key, recursion);
    
    Recursion newRecursion = docStore.find(key);
    assertThat(newRecursion.key).isEqualTo(recursion.key);
    assertThat(newRecursion.start.key).isEqualTo(recursion.start.key);
    assertThat(newRecursion.start.b.key).isEqualTo(recursion.start.b.key);
    assertThat(newRecursion.start.b).isEqualTo(newRecursion.start.b.a.b);
  }
  
  @Test
  public void javaTyping()
  {
    ObjectStore<Zoo> docStore = storeOf(Zoo.class);
    Zoo zoo = JavaTyping.createZoo();
    Long key = 456l;
    docStore.merge(key, zoo);
    
    Zoo newZoo = docStore.find(key);
    assertThat(newZoo).isNotNull();
    assertThat(newZoo.a).isInstanceOf(Lion.class);
    assertThat(((Lion)newZoo.a).key).isEqualTo("Simba");
    assertThat(newZoo.b).isInstanceOf(Elephant.class);
    assertThat(((Elephant)newZoo.b).key).isEqualTo("Benjamin");
    assertThat(newZoo.c).isInstanceOf(Labrador.class);
    assertThat(((Labrador)newZoo.c).key).isEqualTo("Lassi");
    
    Animal newBeethoven = newZoo.d.iterator().next();
    assertThat(newZoo.e.get("first")).isSameAs(newBeethoven);
    assertThat(newZoo.d).hasSameSizeAs(zoo.d);
    Labrador newLabrador = (Labrador)newZoo.e.get("first");
    Labrador labrado = (Labrador)zoo.e.get("first");
    
    // FIXME: 
 /*   isEqual(newLabrador.likesToPlayWith, labrado.likesToPlayWith);
  }
  
  private static <T> void isEqual(Collection<T> first, Collection<T> second)
  {
    assertThat(first).hasSameSizeAs(second);
    Iterator<T> it1 = first.iterator();
    Iterator<T> it2 = second.iterator();
    for(;it1.hasNext();)
    {
      T e1 = it1.next();
      T e2 = it2.next();
      assertThat(e1).isEqualToComparingFieldByField(e2);
    }*/
  }
  
  @Test
  public void sameReference()
  {
    SameReference reference = SameReference.createReferenceTest();
    ObjectStore<SameReference> docStore = storeOf(SameReference.class);
    Long key = 789l;
    docStore.merge(key, reference);
    
    SameReference newRef = docStore.find(key);
    assertThat(newRef.key).isEqualTo("Start");
    assertThat(newRef.ref1.key).isEqualTo("X");
    assertThat(newRef.ref1).isSameAs(newRef.ref2);
  }
  
  private <T> ObjectStore<T> storeOf(Class<T> type)
  {
      Serializer<T> serializer = getSerializerWithCorrectType(type);
      return new MemoryJsonStore<>(serializer);
  }

  private <T> Serializer<T> getSerializerWithCorrectType(Class<T> type)
  {
    try
    {
      // get serializer with correct type the hacky way:
      Constructor<?> constructor = getSerializer(type).getClass().getDeclaredConstructors()[0];
      @SuppressWarnings("unchecked")
      Serializer<T> serializer = (Serializer<T>) constructor.newInstance(type);
      return serializer;
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  
}
