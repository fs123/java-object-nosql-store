package ch.ivyteam.serialize;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.PROPERTY,
	    property = "type")
	@JsonSubTypes({
	    @Type(value = JavaTyping.Lion.class, name = "a"),
	    @Type(value = JavaTyping.Elephant.class, name = "b"),
	    @Type(value = JavaTyping.Labrador.class, name = "c")})
public class JavaTyping {
	
	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.a = new Lion("Simba");
		z.b = new Elephant("Benjamin");
		z.c = new Labrador("Lassi");
		return z;
	}
	
	public static class Zoo {
		public Animal a;
		public Animal b;
		public Dog c;
	}

	public static class Lion implements Animal {
		public String key;
		public Lion(String key) {
			this.key = key;
		}
	}
	
	public static class Elephant implements Animal {
		public String key;
		public Elephant(String key) {
			this.key = key;
		}
	}
	
	public static class Labrador extends Dog {
		public String key;
		public Labrador(String key) {
			this.key = key;
		}
	}
	
	public static abstract class Dog implements Animal { }
	
	public interface Animal { }
}
