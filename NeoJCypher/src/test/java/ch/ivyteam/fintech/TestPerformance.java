package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.jcypher.CypherDocs;
import ch.ivyteam.java.object.jcypher.DBAccess;
import iot.jcypher.concurrency.Locking;
import iot.jcypher.domain.DomainAccessFactory;
import iot.jcypher.domain.IDomainAccess;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance
{

  private static final int DOSSIERS_PER_YEAR = 200_000;

  @Test
  public void t1_fillData()
  {
    CypherDocs<Dossier> store = storeOf(Dossier.class);
    for(int i=0; i<DOSSIERS_PER_YEAR*10; i++)
    {
      Dossier dossier = RandomDossier.generate();
    }
    // sorry even java only execution to generate takes ~ 170 seconds! :-(
    
    // persist me!
  }
  
  @Test
  public void t2_readComplexDossier()
  {
    Dossier dossier = new Dossier(); // read me from store;
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  @Test
  public void t3_queryComplexDossier()
  {
    Dossier dossier = new Dossier(); // query me from store
    // where beneficiation firstname = "Aaron"
    // and controllingPersonMgmt.fiduciarayHolssingAssets = true
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  private static <T> CypherDocs<T> storeOf(Class<T> type)
  {
    IDomainAccess domainAccess = DomainAccessFactory.createDomainAccess(DBAccess.localInstance(), "MERGE");
    domainAccess.setLockingStrategy(Locking.NONE);
    return new CypherDocs<>(domainAccess, type);
  }
  
}
