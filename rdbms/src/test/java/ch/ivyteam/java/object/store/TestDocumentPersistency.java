package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ch.ivyteam.college.Student;
import ch.ivyteam.java.object.store.rdbms.DocumentSchema;
import ch.ivyteam.java.object.store.rdbms.DocumentSchema.DbType;
import ch.ivyteam.java.object.store.rdbms.SysDbRepository;
import ch.ivyteam.java.object.store.rdbms.TstConnectionFactory;

@RunWith(Parameterized.class)
public class TestDocumentPersistency
{
  @Test
  public void storeAndLoad()
  {
    BusinessDataRepository<Dossier> docStore = storeOf(Dossier.class);
   
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
    BusinessDataRepository<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist(new Dossier());
    dossierStore.persist(new Dossier());
    
    BusinessDataRepository<Student> studentStore = storeOf(Student.class);
    Student student = new Student();
    student.firstname = "Reguel";
    studentStore.persist(student);
    
    Collection<Dossier> dossiers = dossierStore.findAll();
    assertThat(dossiers).hasSize(2);
    
    Collection<Student> persistentStudents = studentStore.findAll();
    assertThat(persistentStudents).hasSize(1);
    assertThat(persistentStudents.iterator().next()).isEqualToComparingFieldByField(student);
  }
  
  @Test
  public void query()
  {
    BusinessDataRepository<Dossier> store = storeOf(Dossier.class);
    Dossier myDossier = new Dossier();
    myDossier.name = "Hi Again";
    store.persist(myDossier);
    
    BusinessDataRepository.Filters filters = new BusinessDataRepository.Filters();
    filters.field("name", "Hi Again");
    Collection<Dossier> dossiers = store.query(filters);
    assertThat(dossiers).hasSize(1);
    assertThat(dossiers.iterator().next()).isEqualToComparingFieldByField(myDossier);
  }
  
  @Test
  public void bulkInsert()
  {
    BusinessDataRepository<Dossier> repository = storeOf(Dossier.class);
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
  
  private <T> BusinessDataRepository<T> storeOf(Class<T> type)
  {
    return new SysDbRepository<>(type, connection);
  }
  
  @Parameter
  public DbType dbType;
  
  @Parameters(name = "{index}: rdbms - {0}")
  public static Collection<Object[]> data() {
    List<Object[]> d = new ArrayList<>();
    for(DbType type : Arrays.asList(DbType.values()))
    {
      d.add(new Object[]{type});
    }
    return d;
  }
  
  private Connection connection;
  
  @Before
  public void setup()
  {
    connection =  TstConnectionFactory.forDbms(dbType);
    new DocumentSchema(connection).create(dbType);
  }
  
  @After
  public void tearDown()
  {
    new DocumentSchema(connection).drop(dbType);
    try
    {
      connection.close();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
}
