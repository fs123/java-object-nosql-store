package ch.ivyteam.college.neo4j.v2;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity(label="Class")
public class Course extends Entity{
  
    private String name;
    
    @Relationship(type=Relations.SUBJECT_TAUGHT)
    private Subject subject;
    
    @Relationship(type=Relations.TEACHES_CLASS, direction=Relationship.INCOMING)
    private Teacher teacher;
    
    @Relationship(type=Relations.ENROLLED, direction=Relationship.INCOMING)
    private Set<Enrollment> enrollments = new HashSet<>();
    
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

