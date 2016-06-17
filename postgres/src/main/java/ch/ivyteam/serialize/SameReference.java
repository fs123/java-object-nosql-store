package ch.ivyteam.serialize;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * SameReference -> ref1 -> x
 *               -> ref2 -> x
 */
public class SameReference {

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
	
	public SameReference(String key) {
		this.key = key;
	}
	
	public SameReference() {
	}
	
	@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id-TypeX")
	public static class TypeX {
		public String key;
		public TypeX(String key) {
			this.key = key;
		}
		public TypeX() {
		}
	}
}
