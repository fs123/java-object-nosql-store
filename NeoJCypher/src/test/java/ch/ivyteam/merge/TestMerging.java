package ch.ivyteam.merge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import ch.ivyteam.fintech.AccountHolder;
import ch.ivyteam.fintech.BeneficialOwner;
import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.Person;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.java.object.jcypher.CypherDocs;
import ch.ivyteam.java.object.store.Documents;
import iot.jcypher.concurrency.Locking;
import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;
import iot.jcypher.domain.DomainAccessFactory;
import iot.jcypher.domain.IDomainAccess;

public class TestMerging
{

  private static IDomainAccess domainAccess;

  @Test
  public void testIndependentChanges()
  {
    Dossier dossier = RandomDossier.generate();
    dossier.accountHolder.bankBranch = "first branch";
    CypherDocs<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist(null, dossier);
    String key = String.valueOf(dossierStore.domain.getSyncInfo(dossier).getId());
    
    CypherDocs<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(key);
    CypherDocs<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(key);
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    modifyAccountHolder(dossierStore, loadedDossier);
    modifyBeneficians(dossierStore, loadedDossier2);
    
    Dossier mergedDossier = storeOf(Dossier.class).find(key);
    assertThat(mergedDossier).isNotSameAs(loadedDossier).isNotSameAs(loadedDossier2);
    
    assertThat(mergedDossier.accountHolder.bankBranch).isEqualTo("new Branch");
    assertThat(mergedDossier.accountHolder.dateOfRegistry).isEqualTo(dossier.accountHolder.dateOfRegistry);
    
    Person firstBenefician = mergedDossier.beneficialOwners.iterator().next().person;
    assertThat(firstBenefician.lastName).isEqualTo("New Married Name");
    assertThat(firstBenefician.firstName).isEqualTo(dossier.beneficialOwners.iterator().next().person.firstName);
  }

  private static void modifyBeneficians(Documents<Dossier> store, Dossier dossier)
  {
    BeneficialOwner firstBenefician = dossier.beneficialOwners.iterator().next();
    firstBenefician.person.lastName = "New Married Name";
    store.persist("111", dossier);
  }

  private static void modifyAccountHolder(Documents<Dossier> store, Dossier dossier)
  {
    AccountHolder accountHolder = dossier.accountHolder;
    accountHolder.bankBranch = "new Branch";
    store.persist("111", dossier);
  }
  
  @Test
  public void modifyConcurrentDifferentFieldsOnSameObject()
  {
    Dossier dossier = RandomDossier.generate();
    CypherDocs<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist(null, dossier);
    String key = String.valueOf(dossierStore.domain.getSyncInfo(dossier).getId());
    
    CypherDocs<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(key);
    CypherDocs<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(key);
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    dossierStore.persist(null, loadedDossier);
    loadedDossier2.accountHolder.contactPerson = "Fritz";
    dossierStore.persist(null, loadedDossier2);
    
    Dossier mergedDossier = storeOf(Dossier.class).find(key);
    assertThat(mergedDossier.accountHolder.name).isEqualTo("Theo");
    assertThat(mergedDossier.accountHolder.contactPerson).isEqualTo("Fritz");
    assertThat(mergedDossier.accountHolder.bankBranch).isEqualTo(dossier.accountHolder.bankBranch);
  }
  
  @Test
  public void modifyConcurrentDifferentFieldsOnSameObject_fail()
  {
    Dossier dossier = RandomDossier.generate();
    Documents<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist("333", dossier);
    
    Dossier loadedDossier = dossierStore.find("333");
    Dossier loadedDossier2 = dossierStore.find("333");
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    dossierStore.persist("333", loadedDossier);
    try
    {
      loadedDossier2.accountHolder.contactPerson = "Fritz";
      dossierStore.persist("333", loadedDossier2);
      Assertions.failBecauseExceptionWasNotThrown(Throwable.class);
    }
    catch (Throwable th)
    {
      assertThat(th).hasMessageContaining("warn me!");
    }
  }
  
  @Test
  public void modifyConcurrentSameField()
  {
    Dossier dossier = RandomDossier.generate();
    Documents<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist("444", dossier);
    
    Dossier loadedDossier = dossierStore.find("444");
    Dossier loadedDossier2 = dossierStore.find("444");
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    dossierStore.persist("444", loadedDossier);
    try
    {
      loadedDossier2.accountHolder.name = "Fritz";
      dossierStore.persist("444", loadedDossier2);
      Assertions.failBecauseExceptionWasNotThrown(Throwable.class);
    }
    catch (Throwable th)
    {
      assertThat(th).hasMessageContaining("fail me!");
    }
  }
  
  
  private static <T> CypherDocs<T> storeOf(Class<T> type)
  {
    IDBAccess embedded = realNeo4j();
    domainAccess = DomainAccessFactory.createDomainAccess(embedded, "MERGE");
    domainAccess.setLockingStrategy(Locking.OPTIMISTIC);
    return new CypherDocs<>(domainAccess, type);
  }

  public static IDBAccess realNeo4j()
  {
    Properties remoteProperties = new Properties();
    remoteProperties.put(DBProperties.SERVER_ROOT_URI, "http://@localhost:7474");
    IDBAccess embedded = DBAccessFactory.createDBAccess(DBType.REMOTE, remoteProperties, "neo4j", "X.ivyi1.k");
    return embedded;
  }
  
}
