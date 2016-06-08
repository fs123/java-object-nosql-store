package ch.ivyteam.college;

import java.util.Set;

public class Student {
    private String name;
    private Set<Enrollment> enrollments;
    
    public String getName()
    {
      return name;
    }
    public void setName(String name)
    {
      this.name = name;
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

