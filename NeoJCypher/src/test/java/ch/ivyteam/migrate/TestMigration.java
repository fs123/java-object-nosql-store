package ch.ivyteam.migrate;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.jcypher.CypherDocs;
import ch.ivyteam.java.object.jcypher.DBAccess;
import iot.jcypher.concurrency.Locking;
import iot.jcypher.domain.DomainAccessFactory;
import iot.jcypher.domain.IDomainAccess;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMigration
{

//  @Test
//  public void t1_persistInV1Model()
//  {
//    Student student = new Student();
//    student.sId = 123;
//    student.firstname = "Marco2";
//    student.lastname = "Studer";
//    student.address = "Bahnhofstrasse 2";
//    student.zip = "6300";
//    student.city = "Zug";
//    student.email = "m.s.superman@gmx.ch";
//    Calendar calendar = Calendar.getInstance();
//    calendar.set(1990, 0, 12);
//    student.birthDate = calendar.getTime();
//    
//    Set<Enrollment> enrollments = new HashSet<>();
//    Enrollment enrollment = new Enrollment();
//    Course course = new Course();
//    course.setName("HappyBuilding2");
//    enrollment.setCourse(course);
//    enrollments.add(enrollment);
//    student.setEnrollments(enrollments);
//    
//    CypherDocs<Student> store = storeOf(Student.class);
//    store.persist(null, student);
//    long key = store.domain.getSyncInfo(student).getId();
//    Student loadedStudent = store.find(String.valueOf(key));
//    
//    assertThat(loadedStudent.city).isEqualTo("Zug");
//  }
  
//  @Test
//  public void t2_loadInV1andStoreAsV2Model()
//  {
//    CypherDocs<Student> store = storeOf(Student.class);
//    Student student = store.find("12");
//    
//    assertThat(student).as("De-serialization should even tough not all persistent fields are still in use.").isNotNull();
//    assertThat(student.firstname).isEqualTo("Marco2");
//    assertThat(student.address2).isNull(); // can anyone provide an automatic conversion into the new model?
//    assertThat(student.sId).isEqualTo(123); // simple type change
//    assertThat(student.getEnrollments().iterator().next().getCourse().getName()).isEqualTo("HappyBuilding2");
//    
//    student.address2 = new Address();
//    student.address2.city = "Zug"; // query from old!
//    student.address2.zip = "6300";
//    student.address2.street = "Baarerstrasse 12";
//    student.lastname = "Weber";
//    
//    store.persist(null, student);
//  }
  
//  @Test
//  public void t2_loadInV2Model_fromV1AndV2()
//  {
//    CypherDocs<Student> store = storeOf(Student.class);
//    
//    Student studV1 = store.find("1");
//    assertThat(studV1).as("De-serialization should even tough not all persistent fields are still in use.").isNotNull();
//    assertThat(studV1.firstname).isEqualTo("Marco");
//    assertThat(studV1.address2).isNull(); // can anyone provide an automatic conversion into the new model?
//    assertThat(studV1.sId).isEqualTo(123); // simple type change
//    assertThat(studV1.getEnrollments().iterator().next().getCourse().getName()).isEqualTo("HappyBuilding");
//    
//    Student studV2 = store.find("12");
//    assertThat(studV2.firstname).isEqualTo("Marco2");
//    assertThat(studV2.lastname).isEqualTo("Weber");
//    assertThat(studV2.address2.city).isEqualTo("Zug");
//    assertThat(studV2.address2.zip).isEqualTo("6300");
//    assertThat(studV2.address2.street).isEqualTo("Baarerstrasse 12");
//    assertThat(studV2.sId).isEqualTo(123); // simple type change
//    assertThat(studV2.getEnrollments().iterator().next().getCourse().getName()).isEqualTo("HappyBuilding2");
//  }
  
  private static <T> CypherDocs<T> storeOf(Class<T> type)
  {
    IDomainAccess domainAccess = DomainAccessFactory.createDomainAccess(DBAccess.localInstance(), "MIGRATE");
    domainAccess.setLockingStrategy(Locking.NONE);
    return new CypherDocs<>(domainAccess, type);
  }
  
}
