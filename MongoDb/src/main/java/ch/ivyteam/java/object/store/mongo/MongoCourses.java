package ch.ivyteam.java.object.store.mongo;

import java.util.List;

import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import com.mongodb.DBCollection;

import ch.ivyteam.college.Course;
import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.mongo.MongoCourses.CourseSearchFields;

public class MongoCourses implements Documents<Course, CourseSearchFields>
{
  public enum CourseSearchFields {
    
  }
  
  private JacksonDBCollection<Course, String> coll;

  public MongoCourses(DBCollection collection)
  {
    this.coll = JacksonDBCollection.wrap(collection, Course.class, String.class);
  }

  @Override
  public String persist(String key, Course obj)
  {
    WriteResult<Course, String> writeResult = coll.insert(obj);
    return writeResult.getSavedId();
  }

  @Override
  public Course findById(String key)
  {
    return coll.findOneById(key);
  }

  @Override
  public void remove(String key)
  {
    coll.removeById(key);
  }

  @Override
  public List<Course> query(Filters filters)
  {
    
    return null;
  }
}
