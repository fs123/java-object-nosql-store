package ch.ivyteam.college;

import java.util.Set;

public class Department {
    private String name;
    private Set<Subject> subjects;
    
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

