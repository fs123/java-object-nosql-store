package ch.ivyteam.serialize.neo4j;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.typeconversion.AttributeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ch.ivyteam.neo4j.domain.Entity;

public class JavaTyping {
	
	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.a = new Lion("Simba");
		z.b = new Elephant("Benjamin");
		z.c = new Labrador("Lassi");
		
		Labrador beethoven = new Labrador("Bethoven");
                beethoven.likesToPlayWith = Arrays.asList(z.c, z.c);
                
                z.d = new HashSet<>(Arrays.asList(beethoven, new Elephant("Graui")));
                z.e = new HashMap<>();
                z.e.put("first", beethoven);
		
		return z;
	}
	
	@NodeEntity
	public static class Zoo extends Entity{
		public Animal a;
		public Animal b;
		public Dog c;
		public Set<Animal> d;
		@Convert(value=JsonDogMapConverter.class)
                public Map<String, Dog> e;
	}

	@NodeEntity
	public static class Lion extends Entity implements Animal {
		public String key;
		
		public Lion(){}
		public Lion(String key) {
			this.key = key;
		}
	}
	
	@NodeEntity
	public static class Elephant extends Entity implements Animal {
		public String key;
		public Elephant(){}
		public Elephant(String key) {
			this.key = key;
		}
	}
	
	@NodeEntity
	public static class Labrador extends Dog {
		public String key;
		@GraphId
		public Long id;
		
		public List<Animal> likesToPlayWith;
		
		public Labrador(){}
		public Labrador(String key) {
			this.key = key;
		}
	}
	
	public static abstract class Dog implements Animal { }
	
	public interface Animal { }
	
	public static class JsonDogMapConverter implements AttributeConverter<Map<String,Dog>, String>
	{
	  @Override
	  public Map<String,Dog> toEntityAttribute(String json)
	  {
	    
	    
	    Type type = new TypeToken<Map<String, Labrador>>(){}.getType();
	    return new Gson().fromJson(json, type);
	  }

	  @Override
	  public String toGraphProperty(Map<String,Dog> object)
	  {
	    return new Gson().toJson(object);
	  }
	}
}
