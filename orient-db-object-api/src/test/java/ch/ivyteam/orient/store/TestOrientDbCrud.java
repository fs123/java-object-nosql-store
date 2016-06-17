package ch.ivyteam.orient.store;

import java.util.GregorianCalendar;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

import ch.ivyteam.orient.store.Account;
import ch.ivyteam.orient.store.Address;
import ch.ivyteam.orient.store.City;
import ch.ivyteam.orient.store.Country;

public class TestOrientDbCrud {

	@Test
	public void testCrud() throws Exception {
		Account account2;
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {

			// CREATE A NEW OBJECT AND FILL IT
			account2 = new Account();
			account2.setName("Luke2");
			account2.setSurname("Skywalker2");
			account2.setFirstname("FistName2");
			account2.setBirthdate(new GregorianCalendar(1983, 5, 4).getTime());

			City rome2 = new City("Rome", new Country("Italy2"));
			account2.getAddresses().add(new Address("Residence2", rome2, "Piazza Navona, 2"));

			// SAVE THE ACCOUNT: THE DATABASE WILL SERIALIZE THE OBJECT AND GIVE
			// THE PROXIED INSTANCE
			account2 = db.save(account2);

			db.detach(account2);

			account2.setName("Luke changed");
			account2.getAddresses().get(0).getCity().setName("Rome changed");
			account2.getAddresses()
					.add(new Address("Residence3", account2.getAddresses().get(0).getCity(), "Piazza Navona, 3"));
			account2.getAddresses().add(account2.getAddresses().get(0));

		}
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			System.out.println(db.getIdentity(account2));
			db.save(account2);
		}

	}

	@Test
	public void testRead() throws Exception {
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			ORecordId recordId = new ORecordId("#54:1");
			Account account = db.load(recordId);
			Assertions.assertThat(account.getBirthdate()).isEqualTo(new GregorianCalendar(1983, 5, 4).getTime());
		}
	}

	@Test
	public void testQuery() throws Exception {

		try (OObjectDatabaseTx db = openAndRegisterEntities()) {
			List<Account> result = db
					.query(new OSQLSynchQuery<Account>("select * from Account where name like 'Luke%'"));
			for (Account account : result) {

				System.out.println(account.getName());
			}
		}
	}

	private OObjectDatabaseTx openAndRegisterEntities() {
		OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/petstore");
		db.open("root", "admin");
		// REGISTER THE CLASS ONLY ONCE AFTER THE DB IS OPEN/CREATED
		db.getEntityManager().registerEntityClasses("ch.ivyteam.orient.store");
		return db;
	}

}
