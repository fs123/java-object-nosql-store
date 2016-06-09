package ch.ivyteam.college;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Student {
    public String firstname;
    public String lastname;
    
    public String address;
    public String zip;
    public String city;

    public String email;
    public Date birthDate;
    
    public Map<String, Object> settings = new HashMap<>();
    
    private Set<Enrollment> enrollments;
    
    public Set<Enrollment> getEnrollments()
    {
      return enrollments;
    }
    public void setEnrollments(Set<Enrollment> enrollments)
    {
      this.enrollments = enrollments;
    }
}

