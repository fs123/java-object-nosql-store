package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import ch.ivyteam.college.neo4j.Course;
import ch.ivyteam.college.neo4j.Department;
import ch.ivyteam.college.neo4j.IvyCollege;
import ch.ivyteam.college.neo4j.Student;
import ch.ivyteam.java.object.store.Documents.Filters;

public class TestDocumentPersistency
{
  
  @Test
  public void storeAndLoadSinglePojo()
  {
    Department zug = IvyCollege.getDepZug();
    
    assertThat(zug.getId()).isNull();
    Documents<Department> docStore = new GraphDocPersistency().get(Department.class, "");
    docStore.persist(String.valueOf(zug.getId()), zug);
    assertThat(zug.getId()).isNotNull();
  }

  @Test
  public void storeAndLoadComplexTree()
  {
    Documents<Course> docStore = new GraphDocPersistency().get(Course.class, "");
   
    Course rx = IvyCollege.getRx();
    docStore.persist(String.valueOf(rx.getId()), rx);
    
    Documents<Student> studentStore = new GraphDocPersistency().get(Student.class, "");
    Filters dreFilter = new Filters();
    dreFilter.fieldFilter("name", "Dominik Regli");
    List<Student> matchingStudents = studentStore.query(dreFilter);
    assertThat(matchingStudents).hasSize(1);
  }
  
}
