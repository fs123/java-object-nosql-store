package ch.ivyteam.orient.serialize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaTyping {

	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.setA(new Lion("Simba"));
		z.setB(new Elephant("Benjamin"));
		z.setC(new Labrador("Lassi"));

		Labrador beethoven = new Labrador("Bethoven");
		beethoven.setLikesToPlayWith(Arrays.asList(z.getC(), z.getC()));

		z.setD(new HashSet<>(Arrays.asList(beethoven, new Elephant("Graui"))));
		z.setE(new HashMap<>());
		z.getE().put("first", beethoven);

		return z;
	}

	public static class Zoo {
		private Animal a;
		private Animal b;
		private Dog c;
		private Set<Animal> d;
		private Map<String, Dog> e;

		public Animal getA() {
			return a;
		}

		public void setA(Animal a) {
			this.a = a;
		}

		public Animal getB() {
			return b;
		}

		public void setB(Animal b) {
			this.b = b;
		}

		public Dog getC() {
			return c;
		}

		public void setC(Dog c) {
			this.c = c;
		}

		public Set<Animal> getD() {
			return d;
		}

		public void setD(Set<Animal> d) {
			this.d = d;
		}

		public Map<String, Dog> getE() {
			return e;
		}

		public void setE(Map<String, Dog> e) {
			this.e = e;
		}

	}

	public static class Lion implements Animal {
		private String key;

		public Lion() {
		}

		public Lion(String key) {
			this.setKey(key);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	public static class Elephant implements Animal {
		private String key;

		public Elephant() {
		}

		public Elephant(String key) {
			this.setKey(key);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	public static class Labrador extends Dog {
		private String key;

		private List<Animal> likesToPlayWith;

		public Labrador() {
		}

		public Labrador(String key) {
			this.setKey(key);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public List<Animal> getLikesToPlayWith() {
			return likesToPlayWith;
		}

		public void setLikesToPlayWith(List<Animal> likesToPlayWith) {
			this.likesToPlayWith = likesToPlayWith;
		}
	}

	public static abstract class Dog implements Animal {
	}

	public interface Animal {
	}
}
