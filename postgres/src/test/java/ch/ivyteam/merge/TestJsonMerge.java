package ch.ivyteam.merge;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.cedarsoftware.util.io.JsonReader;

public class TestJsonMerge {

  @Test
  public void testMissingField() 
  {
  	TestClass expected = new TestClass();
  	expected.field1 = "aa";
  	expected.field2 = "bb";
  	
  	String json = "{\"@type\":\"ch.ivyteam.merge.TestJsonMerge$TestClass\",\"fieldX\":\"bb\",\"field1\":\"aa\",\"field2\":\"bb\",\"fieldZ\":\"bb\"}";
  	TestClass current = (TestClass) JsonReader.jsonToJava(json);
  	
  	assertThat(current).isEqualToComparingFieldByField(expected);
  }

  @Test
  public void testObsoleteField() 
  {
  	TestClass expected = new TestClass();
  	expected.field1 = "aa";
  	
  	String json = "{\"@type\":\"ch.ivyteam.merge.TestJsonMerge$TestClass\",\"fieldX\":\"bb\",\"field1\":\"aa\"}";
  	TestClass current = (TestClass) JsonReader.jsonToJava(json);
  	
  	assertThat(current).isEqualToComparingFieldByField(expected);
  }

  public static class TestClass
  {
  	String field1;
  	String field2;
  }
}
