package ch.ivyteam.orient.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance {

	@Test
	public void t2_readComplexDossier() {
		try (OObjectDatabaseTx db = openAndRegisterEntities()) {

			ORecordId recordId = new ORecordId("#121:1");
			Dossier dossier = db.load(recordId);
			// needed if you need all fields filled up
			// dossier = db.detachAll(dossier, false);

			assertThat(dossier).isNotNull();
			assertThat(dossier.getBeneficialOwners().iterator().next().getPerson().getFirstName()).isNotNull();
		}
	}

	@Test
	public void t3_queryComplexDossier() {

		try (OObjectDatabaseTx db = openAndRegisterEntities()) {

			String extendedSQL = "select * from Dossier "
					+ "where beneficialOwners contains (person.firstName LIKE 'Mer%') "
					+ "and controllingPersonMgmt.fiduciaryHoldingAssets = false LIMIT 10";
			List<Dossier> dossiers = db.query(new OSQLSynchQuery<Dossier>(extendedSQL));

			System.out.println(dossiers.size());
			for (Dossier dossier : dossiers) {
				assertThat(dossier).isNotNull();
				assertThat(dossier.getBeneficialOwners().iterator().next().getPerson().getFirstName()).isNotNull();
			}
		}
	}

	private OObjectDatabaseTx openAndRegisterEntities() {
		OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/dossier");
		db.open("root", "admin");
		db.getEntityManager().registerEntityClasses("ch.ivyteam.orient.fintech");
		return db;
	}

}
