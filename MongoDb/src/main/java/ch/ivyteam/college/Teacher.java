package ch.ivyteam.college;

import java.util.Set;

public class Teacher {
    private String name;
    private Set<Course> courses;
    private Set<Subject> subjects;
    
    public String getName()
    {
      return name;
    }
    public void setName(String name)
    {
      this.name = name;
    }
    
    public Set<Course> getCourses()
    {
      return courses;
    }
    public void setCourses(Set<Course> courses)
    {
      this.courses = courses;
    }
    
    public Set<Subject> getSubjects()
    {
      return subjects;
    }
    public void setSubjects(Set<Subject> subjects)
    {
      this.subjects = subjects;
    }
}

