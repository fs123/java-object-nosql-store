package ch.ivyteam.serialize;

public class JavaTyping {
	
	public static Zoo createZoo() {
		Zoo z = new Zoo();
		z.a = new Lion("Simba");
		z.b = new Elephant("Benjamin");
		z.c = new Labrador("Lassi");
		return z;
	}
	
	public static class Zoo {
		public String key;
		public Animal a;
		public Animal b;
		public Dog c;
	}

	public static class Lion implements Animal {
		public String key;
		public Lion() {}
		public Lion(String key) {
			this.key = key;
		}
	}
	
	public static class Elephant implements Animal {
		public String key;
		public Elephant() {}
		public Elephant(String key) {
			this.key = key;
		}
	}
	
	public static class Labrador extends Dog {
		public String key;
		public Labrador() {}
		public Labrador(String key) {
			this.key = key;
		}
	}
	
	public static abstract class Dog implements Animal { }
	
	public interface Animal { }
}
