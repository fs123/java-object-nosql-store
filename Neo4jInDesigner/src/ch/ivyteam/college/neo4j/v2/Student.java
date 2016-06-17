package ch.ivyteam.college.neo4j.v2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import ch.ivyteam.neo4j.JsonMapConverter;
import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Student extends Entity
{
  public String firstname;
  public String lastname;

// no longer used fields from v1
//  public String address;
//  public String zip;
//  public String city;
  
  //new relation from v2
  @Relationship(type=Relations.LOCATED_AT)
  public Address address;

  public String email;
  
  @DateLong
  public Date birthDate;

  @Convert(value=JsonMapConverter.class)
  public Map<String, Object> settings = new HashMap<>();

  @Relationship(type = Relations.ENROLLED)
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
