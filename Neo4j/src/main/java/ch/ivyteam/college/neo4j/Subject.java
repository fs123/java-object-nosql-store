package ch.ivyteam.college.neo4j;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Subject extends Entity{
    private String name;
    
    @Relationship(type=Relations.CURRICULUM, direction = Relationship.INCOMING)
    private Department department;
    
    @Relationship(type=Relations.TAUGHT_BY)
    private Set<Teacher> teachers;
    
    @Relationship(type=Relations.SUBJECT_TAUGHT, direction = Relationship.INCOMING)
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

