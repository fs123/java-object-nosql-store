package ch.ivyteam.college.neo4j.v2;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class School extends Entity{
    private String name;
    
    @Relationship(type=Relations.DEPARTMENT)
    private Set<Department> departments = new HashSet<>();
    
    @Relationship(type=Relations.STAFF)
    private Set<Teacher> staff = new HashSet<>();
    
    public School()
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
    
    public Set<Department> getDepartments()
    {
      return departments;
    }
    public void setDepartments(Set<Department> departments)
    {
      this.departments = departments;
    }
    
    public Set<Teacher> getStaff()
    {
      return staff;
    }
    public void setStaff(Set<Teacher> staff)
    {
      this.staff = staff;
    }
}

