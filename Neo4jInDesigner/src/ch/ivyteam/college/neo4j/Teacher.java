package ch.ivyteam.college.neo4j;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Teacher extends Entity{
    private String name;
    
    @Relationship(type=Relations.TEACHES_CLASS)
    private Set<Course> courses;
    
    @Relationship(type=Relations.TAUGHT_BY, direction=Relationship.INCOMING)
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

