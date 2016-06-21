package ch.ivyteam.college.v2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.college.Enrollment;

public class Student {
// changed type from int to long:
    public long sId; 
  
    public String firstname;
    public String lastname;
    
// removed:
//    public String address;
//    public String zip;
//    public String city;
// added:
    public Address address;
    
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

