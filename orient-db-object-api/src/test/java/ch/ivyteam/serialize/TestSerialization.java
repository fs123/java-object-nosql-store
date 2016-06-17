package ch.ivyteam.serialize;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

import ch.ivyteam.orient.serialize.JavaTyping;
import ch.ivyteam.orient.serialize.JavaTyping.Elephant;
import ch.ivyteam.orient.serialize.JavaTyping.Labrador;
import ch.ivyteam.orient.serialize.JavaTyping.Lion;
import ch.ivyteam.orient.serialize.JavaTyping.Zoo;
import ch.ivyteam.orient.serialize.Recursion;
import ch.ivyteam.orient.serialize.SameReference;
import ch.ivyteam.orient.serialize.SameReference.TypeX;

public class TestSerialization {

	@Test
	public void recursiveReferences() {
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			Recursion recursion = Recursion.createRecursion();

			recursion = db.save(recursion);
			ORID key = db.getIdentity(recursion);

			Recursion newRecursion = db.load(key);
			assertThat(newRecursion.getKey()).isEqualTo(recursion.getKey());
			assertThat(newRecursion.getStart().getKey()).isEqualTo(recursion.getStart().getKey());
			assertThat(newRecursion.getStart().getB().getKey()).isEqualTo(recursion.getStart().getB().getKey());
			assertThat(newRecursion.getStart().getB().getA().getB())
					.isEqualTo(recursion.getStart().getB().getA().getB());
		}
	}

	@Test
	public void javaTyping() {
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			Zoo zoo = JavaTyping.createZoo();

			zoo = db.save(zoo);
			ORID key = db.getIdentity(zoo);

			Zoo newZoo = db.load(key);
			assertThat(newZoo).isNotNull();
			assertThat(newZoo.getA()).isInstanceOf(Lion.class);
			assertThat(((Lion) newZoo.getA()).getKey()).isEqualTo("Simba");
			assertThat(newZoo.getB()).isInstanceOf(Elephant.class);
			assertThat(((Elephant) newZoo.getB()).getKey()).isEqualTo("Benjamin");
			assertThat(newZoo.getC()).isInstanceOf(Labrador.class);
			assertThat(((Labrador) newZoo.getC()).getKey()).isEqualTo("Lassi");

			assertThat(newZoo.getD()).isEqualTo(zoo.getD());
			// equals not correct on this two maps!
			// assertThat(newZoo.getE()).isEqualTo(zoo.getE());
			assertThat(((Labrador) newZoo.getE().get("first")).getLikesToPlayWith())
					.isEqualTo(((Labrador) zoo.getE().get("first")).getLikesToPlayWith());
		}
	}

	@Test
	public void sameReference() {
		SameReference newRef;
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			SameReference reference = SameReference.createReferenceTest();
			assertThat(reference.getRef1()).isSameAs(reference.getRef2());

			reference.setRef2(null);

			reference = db.save(reference);

			// save before does not recognize same reference
			reference.setRef2(reference.getRef1());
			reference = db.save(reference);

			ORID key = db.getIdentity(reference);

			newRef = db.load(key);
			newRef = db.detachAll(newRef, true);
		}

		assertThat(newRef.getKey()).isEqualTo("Start");
		assertThat(newRef.getRef1().getKey()).isEqualTo("X");
		assertThat(newRef.getRef1().getId()).isEqualTo(newRef.getRef2().getId());
		assertThat(newRef.getKey()).isEqualTo("Start");
		assertThat(newRef.getRef1().getKey()).isEqualTo("X");
		assertThat(newRef.getRef1()).isSameAs(newRef.getRef2());
	}

	@Test
	public void sameReferenceWithDbProxies() {
		SameReference newRef;
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			SameReference reference = SameReference.createReferenceTest();
			assertThat(reference.getRef1()).isSameAs(reference.getRef2());

			TypeX typeX = db.newInstance(TypeX.class);
			typeX.setKey("X");
			reference.setRef1(typeX);
			reference.setRef2(typeX);

			reference = db.save(reference);

			newRef = db.load(db.getIdentity(reference));
			newRef = db.detachAll(newRef, true);
		}

		assertThat(newRef.getKey()).isEqualTo("Start");
		assertThat(newRef.getRef1().getKey()).isEqualTo("X");
		assertThat(newRef.getRef1().getId()).isEqualTo(newRef.getRef2().getId());
		assertThat(newRef.getKey()).isEqualTo("Start");
		assertThat(newRef.getRef1().getKey()).isEqualTo("X");
		assertThat(newRef.getRef1()).isSameAs(newRef.getRef2());
	}

	private OObjectDatabaseTx openAndRegisterEntities() {
		OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/serialize");
		db.open("root", "admin");
		db.getEntityManager().registerEntityClasses("ch.ivyteam.orient.serialize");
		return db;
	}
}
