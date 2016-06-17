package ch.ivyteam.college;

import java.util.Date;

public class Enrollment {
    private Student student;
    private Course course;
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