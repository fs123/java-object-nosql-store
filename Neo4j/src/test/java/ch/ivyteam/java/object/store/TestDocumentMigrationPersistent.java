package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.ivyteam.college.neo4j.IvyCollege;
import ch.ivyteam.college.neo4j.Student;
import ch.ivyteam.college.neo4j.v2.Address;
import ch.ivyteam.neo4j.Neo4jSessionFactory;

public class TestDocumentMigrationPersistent
{
//  private static ServerControls server;
//
//  @BeforeClass
//  public static void setUpBeforeClass()
//  {
//    server = TestServerBuilders.newInProcessBuilder().newServer();
//  }
//  
//  @AfterClass
//  public static void tearDownAfterClass()
//  {
//    server.close();
//  }
//  
  @Test
  public void storeAndLoadSinglePojo()
  {
    Student v1Study = writeV1Data();
    readV1asV2DataAndUpdate(v1Study.getId());
    readV2asV1Data(v1Study.getId());
  }

  private Student writeV1Data()
  {
    Student peterV1 = IvyCollege.createPes();
    Documents<Student> docStore = new GraphDocPersistency().get(Student.class, "driver=org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
    docStore.persist(String.valueOf(peterV1.getId()), peterV1);
    assertThat(peterV1.getId()).isNotNull();
    return peterV1;
  }
  
  private void readV1asV2DataAndUpdate(Long studId)
  {
    GraphDocs<ch.ivyteam.college.neo4j.v2.Student> v2DocStore = new GraphDocs<ch.ivyteam.college.neo4j.v2.Student>(
            Neo4jSessionFactory.getInstance().getNeo4jSessionV2(), ch.ivyteam.college.neo4j.v2.Student.class);
    ch.ivyteam.college.neo4j.v2.Student peterV2 = v2DocStore.find(studId);
    assertThat(peterV2).isNotNull();
    assertThat(peterV2.lastname).isEqualTo("Stöckli");
    assertThat(peterV2.address).isNull();
    
    // write new fields
    Address addr = new Address();
    addr.street = "SomewhereOverTheRainbow 3";
    addr.zip = "8090";
    addr.city = "AUAU";
    peterV2.address = addr;
    
    v2DocStore.persist(String.valueOf(peterV2.getId()), peterV2);
  }
  
  private void readV2asV1Data(Long studId)
  {
    Documents<Student> docStore = new GraphDocPersistency().get(Student.class, "driver=org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver");
    Student peterV1 = docStore.find(String.valueOf(studId));
    assertThat(peterV1).as("Sorry this seems not to be supported - even if we do not add a new relation!").isNull();
  }

}
