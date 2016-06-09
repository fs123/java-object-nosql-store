package ch.ivyteam.college.neo4j.v2;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

@RelationshipEntity(type = Relations.ENROLLED)
public class Enrollment
{
  @GraphId
  private Long id;
  
  @StartNode
  private Student student;
  
  @EndNode
  private Course course;
  
  @DateLong
  private Date enrolledDate;

  public Student getStudent()
  {
    return student;
  }

  public void setStudent(Student student)
  {
    this.student = student;
  }

  public Course getCourse()
  {
    return course;
  }

  public void setCourse(Course course)
  {
    this.course = course;
  }

  public Date getEnrolledDate()
  {
    return enrolledDate;
  }

  public void setEnrolledDate(Date enrolledDate)
  {
    this.enrolledDate = enrolledDate;
  }
}