package ch.ivyteam.migration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.college.Course;
import ch.ivyteam.college.Enrollment;
import ch.ivyteam.college.Student;
import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.MemoryDocumentPersistency;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMigration
{

  @Test
  public void t1_persistInV1Model()
  {
    Student student = new Student();
    student.sId = 123;
    student.firstname = "Marco";
    student.lastname = "Studer";
    student.address = "Bahnhofstrasse 2";
    student.zip = "6300";
    student.city = "Zug";
    student.email = "m.s.superman@gmx.ch";
    Calendar calendar = Calendar.getInstance();
    calendar.set(1990, 0, 12);
    student.birthDate = calendar.getTime();
    
    Set<Enrollment> enrollments = new HashSet<>();
    Enrollment enrollment = new Enrollment();
    Course course = new Course();
    course.setName("HappyBuilding");
    enrollment.setCourse(course);
    enrollments.add(enrollment);
    student.setEnrollments(enrollments);
    
    Documents<Student> store = storeOf(Student.class);
    String key = "335577";
    store.persist(key, student);
    Student loadedStudent = store.find(key);
    
    assertThat(loadedStudent.city).isEqualTo("Zug");
  }
  
  @Test
  public void t2_loadInV2Model()
  {
    Documents<ch.ivyteam.college.v2.Student> store = storeOf(ch.ivyteam.college.v2.Student.class);
    ch.ivyteam.college.v2.Student student = store.find("335577");
    
    assertThat(student).as("De-serialization should even tough not all persistent fields are still in use.").isNotNull();
    assertThat(student.firstname).isEqualTo("Marco");
    assertThat(student.address).isNull(); // can anyone provide an automatic conversion into the new model?
    assertThat(student.sId).isEqualTo(123); // simple type change
    assertThat(student.getEnrollments().iterator().next().getCourse().getName()).isEqualTo("HappyBuilding");
  }
  
  private static <T> Documents<T> storeOf(Class<T> type)
  {
    // switch to your real implementation!!!
    return new MemoryDocumentPersistency().get(type, null);
  }
  
}
