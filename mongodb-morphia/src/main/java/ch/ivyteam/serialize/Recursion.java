package ch.ivyteam.serialize;

/**
 * Demonstrates a recursion:
 * <p>
 * start -> a -> b -> a -> ...
 */
public class Recursion {
	public static Recursion createRecursion() {
		Recursion r = new Recursion("Start");
		TypeA a = new TypeA("A");
		TypeB b = new TypeB("B");

		a.b = b;
		b.a = a;
		
		r.start = a;
		return r;
	}
	
	public String key;
	public TypeA start;
	public Recursion(String key) {
		this.key = key;
	}
	
	public static class TypeA {
		public String key;
		public TypeB b;
		public TypeA(String key) {
			this.key = key;
		}
	}

	public static class TypeB {
		public String key;
		public TypeA a;
		public TypeB(String key) {
			this.key = key;
		}
	}
}
