package ch.ivyteam.java.object.store.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ch.ivyteam.college.neo4j.v2.Course;
import ch.ivyteam.college.neo4j.v2.Department;
import ch.ivyteam.college.neo4j.v2.Enrollment;
import ch.ivyteam.college.neo4j.v2.School;
import ch.ivyteam.college.neo4j.v2.Student;
import ch.ivyteam.college.neo4j.v2.Subject;
import ch.ivyteam.college.neo4j.v2.Teacher;

public class SchoolGenerator
{

  private static final int STUDENTS_PER_CLASS = 30;

  public static School createRandomSchool()
  {
    School school = new School();
    school.setName("College "+UUID.randomUUID().timestamp());
    List<Department> departments = createDepartments();
    school.getDepartments().addAll(departments);
    
    List<Subject> subjects = createSubjects();
    assignS(subjects, departments);
    List<Course> courses = createCourses();
    assignC(courses, subjects);
    
    List<Teacher> teachers = new ArrayList<>();
    for(int i=0; i<courses.size()/2; i++)
    {
      school.getStaff().add(randomTeacher());
    }
    teachers.addAll(school.getStaff());
    
    List<Student> students = new ArrayList<>();
    for(int i=0; i<(courses.size()/3)*STUDENTS_PER_CLASS; i++)
    {
      students.add(randomStudent());
    }
    
    int factor = 1;
    for(Course course : courses)
    {
      for(int i=0; i<factor; i++)
      {
        course.setTeacher(randomOf(teachers));
      }
      
      List<Student> candidates = new ArrayList<>(students);
      for(int i=0; i<STUDENTS_PER_CLASS*factor; i++)
      {
        Enrollment enroll = new Enrollment();
        enroll.setCourse(course);
        Student student = randomOf(candidates);
        candidates.remove(student);
        enroll.setStudent(student);
        enroll.setEnrolledDate(new Date());
        course.getEnrollments().add(enroll);
      }
    }
    
    return school;
  }
  
  private static <T> T randomOf(List<T> entities)
  {
    int entry = (int)Math.round(Math.random() * (entities.size()-1));
    return entities.get(entry);
  }
  
  private static void assignC(List<Course> courses, List<Subject> subjects)
  {
    for(Course course : courses)
    {
      int subId = (int)Math.round(Math.random()*(subjects.size()-1));
      subjects.get(subId).getCourses().add(course);
    }
  }

  private static void assignS(List<Subject> subjects, List<Department> departments)
  {
    for(Subject subject : subjects)
    {
      int depId = (int)Math.round(Math.random()*(departments.size()-1));
      departments.get(depId).getSubjects().add(subject);
    }
  }

  private static Student randomStudent()
  {
    Student stud = new Student();
    stud.firstname = Person.getRandomFirstname();
    stud.lastname = Person.getRandomLastname();
    stud.email = stud.firstname.substring(0,1)+"."+stud.lastname+"@education.org";
    stud.address = Addresses.getRandom();
    return stud;
  }
  
  private static Teacher randomTeacher()
  {
    Teacher teacher = new Teacher();
    teacher.setName(Person.getRandomFirstname()+" "+Person.getRandomLastname());
    return teacher;
  }
  
  private static List<Subject> createSubjects()
  {
    return Arrays.asList("Physics", "Chemistry", "Biology", "Earth Science",
            "Pure Mathematics", "Applied Mathematics", "Mechanical Engineering", "Chemical Engineering",
            "Systems Engineering", "Civil Engineering", "Electrical Engineering")
      .stream().map(subject -> {
        Subject s = new Subject();
        s.setName(subject);
        return s;
        })
      .collect(Collectors.toList());
  }
  
  private static List<Department> createDepartments()
  {
    return Arrays.asList("Mathematics", "Science", "Engineering")
      .stream().map(department -> {
        Department d = new Department();
        d.setName(department);
        return d;
        })
      .collect(Collectors.toList());
  }
  
  private static List<Course> createCourses()
  {
    return Arrays.asList(
            "Physics", "Chemistry", "Biology", "Earth Science",
            "Pure Mathematics", "Applied Mathematics",
            "Mechanical Engineering", "Chemical Engineering", "Systems Engineering", "Civil Engineering", "Electrical Engineering")
      .stream().map(course -> {
        Course c = new Course();
        c.setName(course);
        return c;
        })
      .collect(Collectors.toList());
  }
  
}
