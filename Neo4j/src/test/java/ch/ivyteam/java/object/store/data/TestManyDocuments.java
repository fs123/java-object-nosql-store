package ch.ivyteam.java.object.store.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.neo4j.ogm.session.Session;

import ch.ivyteam.college.neo4j.v2.Course;
import ch.ivyteam.college.neo4j.v2.Enrollment;
import ch.ivyteam.college.neo4j.v2.School;
import ch.ivyteam.college.neo4j.v2.Student;
import ch.ivyteam.college.neo4j.v2.Subject;
import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.GraphDocPersistency;
import ch.ivyteam.neo4j.Neo4jSessionFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestManyDocuments
{

  @Test
  public void test1_writeBigData()
  {
    Documents<School> schoolStore = new GraphDocPersistency().get(School.class, null);
    for(int i=0; i<1_000;i++)
    {
      School newSchool = SchoolGenerator.createRandomSchool();
      schoolStore.persist(String.valueOf(newSchool.getId()), newSchool);
    }
  }
  
  @Test
  public void test2_queryLargeDataset()
  {
    StopWatch stopWatch = new StopWatch();
    
    String cypher = "MATCH (s:Student)-[LOCAT_AT]-(a:Address) "
            + "WHERE s.firstname = \"Aaron\" "
            + "AND a.city = \"Cuisia\" "
            + "RETURN s";
    Session session = Neo4jSessionFactory.getInstance().getNeo4jSessionV2();
    stopWatch.start();
    Iterable<Student> studis = session.query(Student.class, cypher, Collections.emptyMap());
    stopWatch.stop();
    assertThat(studis).hasSize(1);
    
    System.out.println("queried in "+stopWatch.getTime()+" ms");
  }
  
  @Test
  public void test3_loadComplexTree()
  {
    StopWatch stopWatch = new StopWatch();
    
    Session session = Neo4jSessionFactory.getInstance().getNeo4jSessionV2();
    stopWatch.start();
    int LOAD_DEPTH = 3; // raise it further to see damn high CPU usage during object mapping :-)
    School aSchool = session.load(School.class, 4174l, LOAD_DEPTH);
    stopWatch.stop();
    assertThat(aSchool).isNotNull();
    Set<Subject> subjects = aSchool.getDepartments().iterator().next().getSubjects();
    assertThat(subjects).isNotEmpty();
    Set<Course> courses = subjects.iterator().next().getCourses();
    assertThat(courses).isNotEmpty();
    Set<Enrollment> enrollments = courses.iterator().next().getEnrollments();
    assertThat(enrollments).isNotEmpty();
    assertThat(enrollments.iterator().next().getStudent()).isNotNull();
    
    assertThat(stopWatch.getTime()).isLessThanOrEqualTo(3000);
  }
  
}
