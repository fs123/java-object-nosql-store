package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.jcypher.CypherDocs;
import ch.ivyteam.java.object.jcypher.DBAccess;
import iot.jcypher.concurrency.Locking;
import iot.jcypher.domain.DomainAccessFactory;
import iot.jcypher.domain.IDomainAccess;
import iot.jcypher.domainquery.DomainQuery;
import iot.jcypher.domainquery.DomainQueryResult;
import iot.jcypher.domainquery.api.DomainObjectMatch;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance
{
  private static final int DOSSIERS_PER_YEAR = 200_000;

  @Test
  public void t1_fillData()
  {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    
    CypherDocs<Dossier> store = storeOf(Dossier.class);
    for (int i = 0; i < DOSSIERS_PER_YEAR * 10; i++)
    {
      Dossier dossier = RandomDossier.generate();
      store.persist(null, dossier);
      if (i % 1000 == 0)
      {
        store = storeOf(Dossier.class); // use another store and let the GC kill used memory
        long duration = stopWatch.getTime();
        System.out.println("Wrote 1'000 Dossiers: " + TimeUnit.MILLISECONDS.toSeconds(duration)+ " sec");
        // ~25 sec for 1'000 dossiers -> with 5 threads = 5'000 Dossiers in 25 secs
      }
    }
  }

  @Test
  public void t2_readComplexDossier()
  {
    CypherDocs<Dossier> store = storeOf(Dossier.class);
    Dossier dossier = store.find("395");

    assertThat(dossier).isNotNull();
    BeneficialOwner beneficialOwner = dossier.beneficialOwners.iterator().next();
    assertThat(beneficialOwner.person.firstName).isNotNull();
  }

  @Test
  public void t3_queryComplexDossier()
  {
    // without index on firstName & lastname -> ~80s
    // with index on first & lastname -> ~ 900 ms
    List<Dossier> dossiers = queryDossiersOf("Kelsey", "Hyden", false);
    Dossier dossier = dossiers.get(0);
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  private static List<Dossier> queryDossiersOf(String firstname, String lastname, Boolean fiduciaryHoldingAssets)
  {
    DomainQuery query = storeOf(Dossier.class).domain.createQuery();
    
    // start with the hardest filter:
    DomainObjectMatch<Person> personMatch = query.createMatch(Person.class);
    query.WHERE(personMatch.atttribute("firstName")).EQUALS(firstname);
    query.WHERE(personMatch.atttribute("lastName")).EQUALS(lastname);

    // navigate to the required root/result object
    DomainObjectMatch<Dossier> dossierMatch = query.TRAVERSE_FROM(personMatch).BACK("person").BACK("beneficialOwners").TO(Dossier.class);
    
    // navigate to controlling tree and filter even more:
    DomainObjectMatch<ControllingPersonManagement> cpmMatch = query.TRAVERSE_FROM(dossierMatch).FORTH("controllingPersonMgmt").TO(ControllingPersonManagement.class);
    query.WHERE(cpmMatch.atttribute("fiduciaryHoldingAssets")).EQUALS(fiduciaryHoldingAssets);
    
    DomainQueryResult result = query.execute();
    return result.resultOf(dossierMatch);
  }

  private static <T> CypherDocs<T> storeOf(Class<T> type)
  {
    IDomainAccess domainAccess = DomainAccessFactory.createDomainAccess(DBAccess.localInstance(), "MERGE");
    domainAccess.setLockingStrategy(Locking.NONE);
    return new CypherDocs<>(domainAccess, type);
  }

}
