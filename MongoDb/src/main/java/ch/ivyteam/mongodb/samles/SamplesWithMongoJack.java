//package ch.ivyteam.mongodb.samles;
//
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.mongojack.Id;
//import org.mongojack.JacksonDBCollection;
//import org.mongojack.ObjectId;
//import org.mongojack.WriteResult;
//
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.MongoClient;
//
///**
// * First simple example with the test database which is already shipped with a plain mongoDB installation.
// * <p>
// * mongojack: https://github.com/mongojack/mongojack
// */
//public class SamplesWithMongoJack
//{
//  public static void main(String[] args)
//  {
//    MongoClient mongoClient = new MongoClient();
//    DB db = mongoClient.getDB("test");
//    
//    DBCollection collection = db.getCollection("dummy");
//    
//    JacksonDBCollection<MyObject, String> coll = JacksonDBCollection.wrap(collection, MyObject.class, String.class);
//
//    MyObject myObject = createMyObject("1");
//    
//    WriteResult<MyObject, String> result = coll.insert(myObject);
//    String id = result.getSavedId();
//    MyObject savedObject = result.getSavedObject();
//
//    System.out.println("inserted entry with ID="+ id +": " + ToStringBuilder.reflectionToString(savedObject));
//  }
//  
//  private static MyObject createMyObject(String key) {
//    MyObject myObject = new MyObject();
//    myObject.stringValue = "ABC-" + key;
//    myObject.numberValue = 123;
//    myObject.child1 = new MyChildObject();
//    myObject.child1.strValue = "A" + key;
//    myObject.child2 = new MyChildObject();
//    myObject.child2.strValue = "B" + key;
//    return myObject;
//  }
//  
//  public static class MyObject {
//    @Id 
//    @ObjectId // required to use findOneById(...)
//    public String id;
//    
//    public String stringValue;
//    public Number numberValue;
//    
//    public MyChildObject child1;
//    public MyChildObject child2;
//    
//  }
//
//  public static class MyChildObject {
//    public String strValue;
//  }
//
//}
