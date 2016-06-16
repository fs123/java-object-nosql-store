package ch.ivyteam.serialize.neo4j;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

public class JavaTyping {
	
	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.a = new Lion("Simba");
		z.b = new Elephant("Benjamin");
		z.c = new Labrador("Lassi");
		return z;
	}
	
	@NodeEntity
	public static class Zoo extends Entity{
		public Animal a;
		public Animal b;
		public Dog c;
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
		public Labrador(){}
		public Labrador(String key) {
			this.key = key;
		}
	}
	
	public static abstract class Dog implements Animal { }
	
	public interface Animal { }
}
