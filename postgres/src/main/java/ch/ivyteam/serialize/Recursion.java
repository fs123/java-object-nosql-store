package ch.ivyteam.serialize;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Demonstrates a recursion:
 * <p>
 * start -> a -> b -> a -> ...
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id-Recursion")
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
	
	public /*final*/ String key;
	public TypeA start;
	public Recursion(String key) {
		this.key = key;
	}
	
	public Recursion() {
		//added const
	}
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id-TypeA")
	public static class TypeA {
		public String key;
		public TypeB b;
		
		public TypeA(String key) {
			this.key = key;
		}
		
		public TypeA() {
			
		}
	}

	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id-TypeB")
	public static class TypeB {
		public String key;
		public TypeA a;
		public TypeB(String key) {
			this.key = key;
		}
		
		public TypeB() {
			
		}

	}
}