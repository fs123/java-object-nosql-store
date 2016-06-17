package ch.ivyteam.college.neo4j;

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

  public String address;
  public String zip;
  public String city;

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
