package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mongojack.DBCursor;
import org.mongojack.Id;
import org.mongojack.JacksonDBCollection;
import org.mongojack.ObjectId;
import org.mongojack.WriteResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import ch.ivyteam.java.object.store.mongo.MongoDocumentConnection;

public class SimpleMongoTest
{
  private JacksonDBCollection<MyObject, String> coll;

  @Before
  public void init()
  {
    DBCollection collection = MongoDocumentConnection.getCollection("MyObject");
    coll = JacksonDBCollection.wrap(collection, MyObject.class, String.class);
  }

  @Test
  public void writeReadAndDelete()
  {
    MyObject myObject = createMyObject("1");

    // CREATE
    WriteResult<MyObject, String> result = coll.insert(myObject);
    
    String id = result.getSavedId();
    MyObject savedObject = result.getSavedObject();

    // READ
    MyObject foundMyObject = coll.findOneById(id);
    
    // assert CREATE / READ
    assertThat(myObject)
      .isNotSameAs(savedObject)
      .isNotSameAs(foundMyObject);

    myObject.id = id;
    assertThat(savedObject)
      .isEqualToComparingFieldByFieldRecursively(myObject);
    
    assertThat(foundMyObject)
      .isEqualToComparingFieldByFieldRecursively(myObject);
    
    // WRITE
    myObject.stringValue = "changed string";
    myObject.child1.strValue = "changed too";
    myObject.child2 = null;
    coll.updateById(id, myObject);

    // assert WRITE / READ
    foundMyObject = coll.findOneById(id);
    assertThat(myObject)
      .isNotSameAs(foundMyObject);
    
    assertThat(foundMyObject)
      .isEqualToComparingFieldByFieldRecursively(myObject);
    
    // DELETE
    coll.removeById(id);
    MyObject foundAfterRemove = coll.findOneById(id);
    assertThat(foundAfterRemove).isNull();
  }
  
  @Test
  public void simpleSearch() 
  {
    String tag = String.valueOf(System.currentTimeMillis());
    
    MyObject myObject = createMyObject(tag);
    coll.insert(myObject);
    
    myObject.numberValue = 234;
    coll.insert(myObject);
    
    myObject.numberValue = 345;
    coll.insert(myObject);
    
    BasicDBObject searchKeys = new BasicDBObject();
    searchKeys.put("stringValue", "ABC-" + tag);

    DBCursor cursor = coll.find(searchKeys);
    assertThat(cursor.count()).isEqualTo(3);
  }
  
  private static MyObject createMyObject(String key) {
    MyObject myObject = new MyObject();
    myObject.stringValue = "ABC-" + key;
    myObject.numberValue = 123;
    myObject.child1 = new MyChildObject();
    myObject.child1.strValue = "A" + key;
    myObject.child2 = new MyChildObject();
    myObject.child2.strValue = "B" + key;
    return myObject;
  }
  
  public static class MyObject {
    @Id
    @ObjectId // required to use findOneById(...)
    public String id;
    
    public String stringValue;
    public Number numberValue;
    
    public MyChildObject child1;
    public MyChildObject child2;
    
  }

  public static class MyChildObject {
    public String strValue;
  }

}
