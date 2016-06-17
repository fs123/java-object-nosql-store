package ch.ivyteam.orient.fintech;

import com.orientechnologies.common.log.OLogManager;
import com.orientechnologies.orient.core.db.ODatabase.OPERATION_MODE;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class FillInData {
	private static final int DOSSIERS_PER_YEAR = 200_000;

	public static void main(String[] args) {

		OLogManager.instance().setInfoEnabled(false);
		long startMillis = System.currentTimeMillis();

		try (OObjectDatabaseTx db = openAndRegisterEntities()) {

			db.getEntityManager().registerEntityClasses("ch.ivyteam.orient.fintech");

			for (int i = 0; i < DOSSIERS_PER_YEAR; i++) {

				db.save(RandomDossier.generate(), (String) null, OPERATION_MODE.ASYNCHRONOUS, true, null, null);

				if (i % 1_000 == 0) {
					long duration = System.currentTimeMillis() - startMillis;
					System.out.println("Generated and saved next 1000 (needed: " + duration + "ms)");
				}
			}
		}
	}

	private static OObjectDatabaseTx openAndRegisterEntities() {
		OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/dossier");
		db.open("root", "admin");
		db.getEntityManager().registerEntityClasses("ch.ivyteam.orient.fintech");
		return db;
	}

}
