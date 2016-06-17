package ch.ivyteam.orient.serialize;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.orientechnologies.orient.core.annotation.OId;

/**
 *
 * SameReference -> ref1 -> x -> ref2 -> x
 */
public class SameReference {

	public static SameReference createReferenceTest() {
		SameReference r = new SameReference("Start");
		TypeX x = new TypeX();
		x.setKey("X");
		r.setRef1(x);
		r.setRef2(x);
		return r;
	}

	@Id
	private Object id;

	private String key;
	@ManyToOne
	private TypeX ref1;
	@ManyToOne
	private TypeX ref2;

	public SameReference() {
	}

	public SameReference(String key) {
		this.setKey(key);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TypeX getRef1() {
		return ref1;
	}

	public void setRef1(TypeX ref1) {
		this.ref1 = ref1;
	}

	public TypeX getRef2() {
		return ref2;
	}

	public void setRef2(TypeX ref2) {
		this.ref2 = ref2;
	}

	public Object getId() {
		return id;
	}

	public static class TypeX {
		@OId
		private Object id;

		private String key;

		public TypeX() {
		}

		public TypeX(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getId() {
			return id;
		}
	}
}
