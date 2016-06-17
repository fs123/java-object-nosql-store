package ch.ivyteam.orient.serialize;

/**
 * Demonstrates a recursion:
 * <p>
 * start -> a -> b -> a -> ...
 */
public class Recursion {
	public static Recursion createRecursion() {
		Recursion r = new Recursion("STart");
		TypeA a = new TypeA("A");
		TypeB b = new TypeB("B");

		a.setB(b);
		b.setA(a);

		r.setStart(a);
		return r;
	}

	private String key;

	public Recursion() {
	}

	public Recursion(String key) {
		this.setKey(key);
	}

	public TypeA getStart() {
		return start;
	}

	public void setStart(TypeA start) {
		this.start = start;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private TypeA start;

	public static class TypeA {
		private String key;
		private TypeB b;

		public TypeA() {
		}

		public TypeA(String key) {
			this.setKey(key);
		}

		public TypeB getB() {
			return b;
		}

		public void setB(TypeB b) {
			this.b = b;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	public static class TypeB {
		private String key;
		private TypeA a;

		public TypeB() {
		}

		public TypeB(String key) {
			this.setKey(key);
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public TypeA getA() {
			return a;
		}

		public void setA(TypeA a) {
			this.a = a;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
			{
				return true;
			}

			if (!(obj instanceof TypeB))
			{
				return false;
			}

			TypeB other = (TypeB) obj;
			return getKey().equals(other.getKey());
		}
	}
}
