package ch.ivyteam.java.object.store.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDocumentConnection
{
  public static final String DB_NAME = "test";
  
  public static final MongoClient mongoClient = new MongoClient();

  @SuppressWarnings("deprecation")
  public static DB getDatabase()
  {
    return mongoClient.getDB(DB_NAME);
  }

  public static DBCollection getCollection(String name)
  {
    return getDatabase().getCollection(name);
  }

  public MongoCourses getMongoCourse()
  {
    DBCollection collection = MongoDocumentConnection.getCollection("courses");
    return new MongoCourses(collection);
  }
}
