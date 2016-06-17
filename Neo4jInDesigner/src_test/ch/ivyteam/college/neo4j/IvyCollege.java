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
    
    Student pes = createPes();
    Student dre = new Student();
    dre.firstname = "Dominik";
    dre.lastname = "Regli";
    Student caty = new Student();
    caty.firstname = "Caty";
    caty.lastname = "Hürlimann";
    
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
  }

  public static Department getDepZug()
  {
    Department zug = new Department();
    zug.setName("Axon.ivy ZUG");
    return zug;
  }

  @SuppressWarnings("deprecation")
  public static Student createPes()
  {
    Student pes = new Student();
    pes.firstname = "Peter";
    pes.lastname = "Stöckli";
    pes.address = "SomewhereOverTheRainbow 3";
    pes.zip = "8090";
    pes.city = "AUAU";
    pes.birthDate = new Date(1986, 9, 2);
    pes.settings.put("coolFeature", Boolean.TRUE);
    pes.settings.put("security", Severity.WARNING);
    return pes;
  }
  
  private static enum Severity
  {
    INFO,
    WARNING,
    ERROR
  }
  
  
}
