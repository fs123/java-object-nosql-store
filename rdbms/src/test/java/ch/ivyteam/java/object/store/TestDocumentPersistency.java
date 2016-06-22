package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ch.ivyteam.college.Student;
import ch.ivyteam.java.object.store.Documents.Filters;
import ch.ivyteam.java.object.store.rdbms.DocumentSchema;
import ch.ivyteam.java.object.store.rdbms.DocumentSchema.DbType;
import ch.ivyteam.java.object.store.rdbms.SysDbStore;
import ch.ivyteam.java.object.store.rdbms.TstConnectionFactory;

@RunWith(Parameterized.class)
public class TestDocumentPersistency
{
  @Test
  public void storeAndLoad()
  {
    Documents<Dossier> docStore = storeOf(Dossier.class);
   
    Dossier doc = new Dossier();
    docStore.persist("2", doc);
    Dossier loadedDoc = docStore.find("2");
    
    assertThat(doc).isEqualToComparingFieldByFieldRecursively(loadedDoc);
    assertThat(docStore.exists("2")).isTrue();
    docStore.remove("2");
    assertThat(docStore.exists("2")).isFalse();
  }
  
  @Test
  public void loadAll()
  {
    Documents<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist(null, new Dossier());
    dossierStore.persist(null, new Dossier());
    
    Documents<Student> studentStore = storeOf(Student.class);
    Student student = new Student();
    student.firstname = "Reguel";
    studentStore.persist(null, student);
    
    Collection<Dossier> dossiers = dossierStore.findAll();
    assertThat(dossiers).hasSize(2);
    
    Collection<Student> persistentStudents = studentStore.findAll();
    assertThat(persistentStudents).hasSize(1);
    assertThat(persistentStudents.iterator().next()).isEqualToComparingFieldByField(student);
  }
  
  @Test
  public void query()
  {
    Documents<Dossier> store = storeOf(Dossier.class);
    Dossier myDossier = new Dossier();
    myDossier.name = "Hi Again";
    store.persist(null, myDossier);
    
    Filters filters = new Filters();
    filters.fieldFilter("name", "Hi Again");
    Collection<Dossier> dossiers = store.query(filters);
    assertThat(dossiers).hasSize(1);
    assertThat(dossiers.iterator().next()).isEqualToComparingFieldByField(myDossier);
  }
  
  public static class Dossier
  {
    public String name = "Hello";
  }
  
  private <T> Documents<T> storeOf(Class<T> type)
  {
    return new SysDbStore<>(type, connection);
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
