package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.neo4j.ogm.session.Session;

import ch.ivyteam.college.neo4j.IvyCollege;
import ch.ivyteam.college.neo4j.Student;
import ch.ivyteam.college.neo4j.v2.Address;
import ch.ivyteam.neo4j.Neo4jSessionFactory;

public class TestDocumentMigrationPersistent
{
	
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
    
    Session sessionV1 = Neo4jSessionFactory.getInstance().getNeo4jSessionV1();
    sessionV1.save(peterV1);
    assertThat(peterV1.getId()).isNotNull();
    return peterV1;
  }
  
  private void readV1asV2DataAndUpdate(Long studId)
  {
    Session sessionV2 = Neo4jSessionFactory.getInstance().getNeo4jSessionV2();
    ch.ivyteam.college.neo4j.v2.Student peterV2 = sessionV2.load(ch.ivyteam.college.neo4j.v2.Student.class, studId);
    assertThat(peterV2).isNotNull();
    assertThat(peterV2.lastname).isEqualTo("Stöckli");
    assertThat(peterV2.address).isNull();
    
    // write new fields
    Address addr = new Address();
    addr.street = "SomewhereOverTheRainbow 3";
    addr.zip = "8090";
    addr.city = "AUAU";
    peterV2.address = addr;
    
    sessionV2.save(peterV2);
  }
  
  private void readV2asV1Data(Long studId)
  {
	  Session sessionV1 = Neo4jSessionFactory.getInstance().getNeo4jSessionV1();
    Student peterV1 = sessionV1.load(Student.class, studId);
    assertThat(peterV1).as("Sorry this seems not to be supported - even if we do not add a new relation!").isNull();
  }

}
