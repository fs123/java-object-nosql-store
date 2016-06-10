package ch.ivyteam.incubator.mongodb.hibernate.ogm;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.TransactionManager;

import org.assertj.core.api.Assertions;
import org.jboss.jandex.Main;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ivyteam.incubator.mongodb.hibernate.ogm.entities.Breed;
import ch.ivyteam.incubator.mongodb.hibernate.ogm.entities.Dog;

public class SimpleCrudTests {

	// build the EntityManagerFactory as you would build in in Hibernate ORM
	private static EntityManagerFactory emf;

	private TransactionManager tm;
	private EntityManager em;

	@BeforeClass
	public static void createEntityManagerFactory() {
		emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		emf.close();
	}

	@Before
	public void setupTransactionManager() throws Exception {
		// accessing JBoss's Transaction can be done differently but this one
		// works nicely
		tm = getTransactionManager();
	}

	@After
	public void rollbackTransaction() {
		try {
			tm.rollback();
		} catch (Exception ex) {

		}
	}

	@Test
	public void testCrud() throws Exception {
		// Persist entities the way you are used to in plain JPA
		tm.begin();
		em = emf.createEntityManager();
		System.out.println("About to store dog and breed");
		Breed collie = new Breed();
		collie.setName("Collie");
		em.persist(collie);
		Dog dina = new Dog();
		dina.setName("Dina");
		dina.setBreed(collie);
		em.persist(dina);
		String dinaId = dina.getId();
		em.flush();
		em.close();
		tm.commit();

		// Retrieve your entities the way you are used to in plain JPA
		tm.begin();
		System.out.println("About to retrieve dog and breed form id: " + dinaId);
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		System.out.printf("Found dog %s of breed %s", dina.getName(), dina.getBreed().getName());
		System.out.println();
		Assertions.assertThat(dina.getName()).isEqualTo("Dina");
		Assertions.assertThat(dina.getBreed().getName()).isEqualTo("Collie");
		em.flush();
		em.close();
		tm.commit();

		// Update with cascading
		tm.begin();
		em = emf.createEntityManager();
		dina.getBreed().setName("Collie2");
		em.merge(dina);
		em.flush();
		em.close();
		tm.commit();

		// Check update
		tm.begin();
		System.out.println("About to retrieve dog and breed form id: " + dinaId);
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		Assertions.assertThat(dina.getBreed().getName()).isEqualTo("Collie2");
		em.flush();
		em.close();
		tm.commit();

		// Delete
		tm.begin();
		em = emf.createEntityManager();
		 // needs to load in same transcation, otherwise you get detached exception
		dina = em.find(Dog.class, dinaId);
		em.remove(dina.getBreed());
		em.remove(dina);
		em.flush();
		em.close();
		tm.commit();

		// Check delete
		String breedId = dina.getBreed().getId();
		tm.begin();
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		Assertions.assertThat(dina).isNull();
		Breed breed = em.find(Breed.class, breedId);
		Assertions.assertThat(breed).isNull();
		em.flush();
		em.close();
		tm.commit();
	}

	@Test
	public void testCrudWithCascading() throws Exception {
		// Persist entities the way you are used to in plain JPA
		tm.begin();
		em = emf.createEntityManager();
		System.out.println("About to store dog and breed");
		Breed collie = new Breed();
		collie.setName("Collie");
		Dog dina = new Dog();
		dina.setName("Dina");
		dina.setBreed(collie);
		em.persist(dina); // only persist dog
		String dinaId = dina.getId();
		em.flush();
		em.close();
		tm.commit();

		// Retrieve your entities the way you are used to in plain JPA
		tm.begin();
		System.out.println("About to retrieve dog and breed form id: " + dinaId);
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		System.out.printf("Found dog %s of breed %s", dina.getName(), dina.getBreed().getName());
		System.out.println();
		Assertions.assertThat(dina.getName()).isEqualTo("Dina");
		Assertions.assertThat(dina.getBreed().getName()).isEqualTo("Collie");
		em.flush();
		em.close();
		tm.commit();

		// Update
		tm.begin();
		em = emf.createEntityManager();
		dina.getBreed().setName("Collie2");
		em.merge(dina); // only merge dog
		em.flush();
		em.close();
		tm.commit();

		// Check update
		tm.begin();
		System.out.println("About to retrieve dog and breed form id: " + dinaId);
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		Assertions.assertThat(dina.getBreed().getName()).isEqualTo("Collie2");
		em.flush();
		em.close();
		tm.commit();

		// Delete
		tm.begin();
		em = emf.createEntityManager();
		// needs to load in same transcation, otherwise you get detached exception
		dina = em.find(Dog.class, dinaId);
		em.remove(dina);
		em.flush();
		em.close();
		tm.commit();

		// Check delete
		String breedId = dina.getBreed().getId();
		tm.begin();
		em = emf.createEntityManager();
		dina = em.find(Dog.class, dinaId);
		Assertions.assertThat(dina).isNull();
		Breed breed = em.find(Breed.class, breedId);
		Assertions.assertThat(breed).isNull();
		em.flush();
		em.close();
		tm.commit();
	}

	@Test
	public void testSelectAllDogs() throws Exception {
		tm.begin();
		em = emf.createEntityManager();

		String qlString = "SELECT dog FROM Dog dog WHERE dog.name LIKE 'Dina'";
		Query query = em.createQuery(qlString);
		List<Dog> resultList = query.getResultList();
		for (Dog dog : resultList) {
			System.out.println(dog);
		}

		em.flush();
		em.close();
		tm.commit();
	}

	private static final String JBOSS_TM_CLASS_NAME = "com.arjuna.ats.jta.TransactionManager";

	public static TransactionManager getTransactionManager() throws Exception {
		Class<?> tmClass = Main.class.getClassLoader().loadClass(JBOSS_TM_CLASS_NAME);
		return (TransactionManager) tmClass.getMethod("transactionManager").invoke(null);
	}
}
