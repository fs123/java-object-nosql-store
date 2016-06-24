package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.ivyteam.college.Student;


public class TestDocumentPersistency extends AbstractRdbmsTest
{
  @Test
  public void storeAndLoad()
  {
    ObjectStore<Dossier> docStore = storeOf(Dossier.class);
   
    Dossier doc = new Dossier();
    long key = 2;
    docStore.merge(key, doc);
    Dossier loadedDoc = docStore.find(key);
    
    assertThat(doc).isEqualToComparingFieldByFieldRecursively(loadedDoc);
    assertThat(docStore.exists(key)).isTrue();
    docStore.delete(key);
    assertThat(docStore.exists(key)).isFalse();
  }
  
  @Test
  public void loadAll()
  {
    ObjectStore<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist(new Dossier());
    dossierStore.persist(new Dossier());
    
    ObjectStore<Student> studentStore = storeOf(Student.class);
    Student student = new Student();
    student.firstname = "Reguel";
    studentStore.persist(student);
    
    Collection<Dossier> dossiers = dossierStore.findAll().values();
    assertThat(dossiers).hasSize(2);
    
    Collection<Student> persistentStudents = studentStore.findAll().values();
    assertThat(persistentStudents).hasSize(1);
    assertThat(persistentStudents.iterator().next()).isEqualToComparingFieldByField(student);
  }
  
  @Test
  public void query()
  {
    ObjectStore<Dossier> store = storeOf(Dossier.class);
    Dossier myDossier = new Dossier();
    myDossier.name = "Hi Again";
    store.persist(myDossier);
    
    Collection<Dossier> dossiers = store.query(
            new ObjectStore.Filters().field("name", "Hi Again")).values();
    assertThat(dossiers).hasSize(1);
    assertThat(dossiers.iterator().next()).isEqualToComparingFieldByField(myDossier);
  }
  
  @Test
  public void bulkInsert()
  {
    ObjectStore<Dossier> repository = storeOf(Dossier.class);
    Long maxId = repository.persist(new Dossier());
    assertThat(maxId).isEqualTo(1);
    
    Dossier a = new Dossier();
    a.name = "first";
    Dossier b = new Dossier();
    b.name = "second";
    List<Dossier> dossiers = Arrays.asList(a, b);
    Set<Long> ids = repository.persist(dossiers);
    assertThat(ids).containsExactly(2l,3l);
  }
  
  public static class Dossier
  {
    public String name = "Hello";
  }
  
}
