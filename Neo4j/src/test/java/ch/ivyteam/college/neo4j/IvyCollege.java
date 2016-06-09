package ch.ivyteam.college.neo4j;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class IvyCollege
{

  @SuppressWarnings("deprecation")
  public static Course getRx()
  {
    // some data
    Department zug = getDepZug();
    
    Student pes = new Student();
    pes.setName("Peter Stöckli");
    Student dre = new Student();
    dre.setName("Dominik Regli");
    Student caty = new Student();
    caty.setName("Caty Hürlimann");
    
    Teacher rwei = new Teacher();
    rwei.setName("Reto Weiss");
    Teacher bb = new Teacher();
    bb.setName("Bruno Bütler");
    Teacher fs = new Teacher();
    fs.setName("Flavio Sadeghi");
    Teacher marcus = new Teacher();
    marcus.setName("Marcus Tandler");
    
    Subject agile = new Subject();
    agile.setName("AgileDev");
    agile.setDepartment(zug);
    
    Course rx = new Course();
    rx.setName("Reactive Streams / RxJava");
    rx.setTeacher(fs);
    rx.setSubject(agile);
    
    Set<Student> students = new HashSet<>();
    students.addAll(Arrays.asList(dre, pes, caty));
    
    Enrollment enrollDreToRx = new Enrollment();
    enrollDreToRx.setCourse(rx);
    enrollDreToRx.setStudent(dre);
    enrollDreToRx.setEnrolledDate(new Date());
    
    Enrollment enrollPesToRx = new Enrollment();
    enrollPesToRx.setCourse(rx);
    enrollPesToRx.setStudent(pes);
    enrollPesToRx.setEnrolledDate(new Date(2007,10,9));
    
    Set<Enrollment> enrollments = new HashSet<>();
    enrollments.addAll(Arrays.asList(enrollDreToRx, enrollPesToRx));
    rx.setEnrollments(enrollments);
    
    Course rise = new Course();
    rise.setName("W-JAX 15: Rise of the machines");
    rise.setTeacher(marcus);
    rise.setSubject(agile);
    
    return rx;
    //// PERSIST ME !!
  }

  public static Department getDepZug()
  {
    Department zug = new Department();
    zug.setName("Axon.ivy ZUG");
    return zug;
  }
  
  
}
