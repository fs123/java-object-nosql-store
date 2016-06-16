package ch.ivyteam.serialize.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

/**
 * Demonstrates a recursion:
 * <p>
 * start -> a -> b -> a -> ...
 */
@NodeEntity
public class Recursion extends Entity {
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
	
	public Recursion()
	{
	}
	
	public Recursion(String key) {
		this.key = key;
	}
	
	@NodeEntity
	public static class TypeA extends Entity{
		public String key;
		public TypeB b;

		public TypeA(){}
		
		public TypeA(String key) {
			this.key = key;
		}
	}

	@NodeEntity
	public static class TypeB extends Entity {
		public String key;
		public TypeA a;
		
		public TypeB(){}
		
		public TypeB(String key) {
			this.key = key;
		}
	}
}
