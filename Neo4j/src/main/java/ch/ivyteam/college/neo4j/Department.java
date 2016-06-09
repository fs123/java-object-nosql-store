package ch.ivyteam.college.neo4j;

import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Department extends Entity{
    private String name;
    
    @Relationship(type=Relations.CURRICULUM)
    private Set<Subject> subjects;
    
    public Department()
    {
    }
    
    public String getName()
    {
      return name;
    }
    public void setName(String name)
    {
      this.name = name;
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
