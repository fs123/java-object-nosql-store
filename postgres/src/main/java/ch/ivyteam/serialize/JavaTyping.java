package ch.ivyteam.serialize;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class JavaTyping {
	
	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.a = new Lion("Simba");
		z.b = new Elephant("Benjamin");
		z.c = new Labrador("Lassi");
		
		Labrador beethoven = new Labrador("Bethoven");
		beethoven.likesToPlayWith = Arrays.asList(z.c, z.c);
		
		z.d = new LinkedHashSet<>(Arrays.asList(beethoven, new Elephant("Graui")));
		z.e = new LinkedHashMap<>();
		z.e.put("first", beethoven);
		
		return z;
	}
	
	public static class Zoo {
		public String key;
		public Animal a;
		public Animal b;
		public Dog c;
		public Set<Animal> d;
		public Map<String, Dog> e;
		
	}

	public static class Lion extends AddToStringHashCodeAndEquals implements Animal {
		public String key;
		public Lion() {}
		public Lion(String key) {
			this.key = key;
		}
	}
	
	public static class Elephant extends AddToStringHashCodeAndEquals implements Animal {
		public String key;
		public Elephant() {}
		public Elephant(String key) {
			this.key = key;
		}
	}
	
	public static class Labrador extends Dog {
		public String key;
		public Labrador() {}
		
		public List<Animal> likesToPlayWith;
		
		public Labrador(String key) {
			this.key = key;
		}
	}
	
	public static abstract class Dog extends AddToStringHashCodeAndEquals implements Animal { }
	
	public interface Animal { }
}