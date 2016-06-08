package ch.ivyteam.college;

import java.util.Set;

public class Course {
    private String name;
    private Subject subject;
    private Teacher teacher;
    private Set<Enrollment> enrollments;
    
    public String getName()
    {
      return name;
    }
    public void setName(String name)
    {
      this.name = name;
    }
    
    public Subject getSubject()
    {
      return subject;
    }
    public void setSubject(Subject subject)
    {
      this.subject = subject;
    }
    
    public Teacher getTeacher()
    {
      return teacher;
    }
    public void setTeacher(Teacher teacher)
    {
      this.teacher = teacher;
    }
    
    public Set<Enrollment> getEnrollments()
    {
      return enrollments;
    }
    public void setEnrollments(Set<Enrollment> enrollments)
    {
      this.enrollments = enrollments;
    }
}

