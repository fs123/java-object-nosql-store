package ch.ivyteam.college.neo4j;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity(label="Class")
public class Course extends Entity{
  
    private String name;
    
    @Relationship(type=Relations.SUBJECT_TAUGHT)
    private Subject subject;
    
    @Relationship(type=Relations.TEACHES_CLASS, direction=Relationship.INCOMING)
    private Teacher teacher;
    
    @Relationship(type=Relations.ENROLLED, direction=Relationship.INCOMING)
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

