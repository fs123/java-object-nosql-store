package ch.ivyteam.serialize.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

/**
 *
 * SameReference -> ref1 -> x
 *               -> ref2 -> x
 */
@NodeEntity
public class SameReference extends Entity {

	public static SameReference createReferenceTest() {
		SameReference r = new SameReference("Start");
		TypeX x = new TypeX("X");
		r.ref1 = x;
		r.ref2 = x;
		return r;
	}

	public String key;
	public TypeX ref1;
	public TypeX ref2;
	
	public SameReference(){}
	
	public SameReference(String key) {
		this.key = key;
	}
	
	@NodeEntity
	public static class TypeX extends Entity {
		public String key;
		
		public TypeX(){}
		
		public TypeX(String key) {
			this.key = key;
		}
	}
}
