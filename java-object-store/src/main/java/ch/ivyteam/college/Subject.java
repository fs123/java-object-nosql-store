package ch.ivyteam.college;

import java.util.Set;

public class Subject {
    private String name;
    private Department department;
    private Set<Teacher> teachers;
    private Set<Course> courses;
    
    public String getName()
    {
      return name;
    }
    public void setName(String name)
    {
      this.name = name;
    }
    
    public Department getDepartment()
    {
      return department;
    }
    public void setDepartment(Department department)
    {
      this.department = department;
    }
    
    public Set<Teacher> getTeachers()
    {
      return teachers;
    }
    public void setTeachers(Set<Teacher> teachers)
    {
      this.teachers = teachers;
    }
    
    public Set<Course> getCourses()
    {
      return courses;
    }
    public void setCourses(Set<Course> courses)
    {
      this.courses = courses;
    }
}

