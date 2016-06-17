package ch.ivyteam.merge.neo4j;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.neo4j.ogm.session.Session;

import ch.ivyteam.fintech.neo4j.AccountHolder;
import ch.ivyteam.fintech.neo4j.BeneficialOwner;
import ch.ivyteam.fintech.neo4j.Dossier;
import ch.ivyteam.fintech.neo4j.Person;
import ch.ivyteam.fintech.neo4j.RandomDossier;
import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.GraphDocs;
import ch.ivyteam.neo4j.Neo4jSessionFactory;

public class TestMerging
{
  @Test
  public void testIndependentChanges()
  {
    Dossier dossier = RandomDossier.generate();
    dossier.accountHolder.bankBranch = "first branch";
    storeOf(Dossier.class).persist(String.valueOf(dossier.getId()), dossier);
    
    Documents<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(String.valueOf(dossier.getId()));
    Documents<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(String.valueOf(dossier.getId()));
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    modifyAccountHolder(store1, loadedDossier);
    modifyBeneficians(store2, loadedDossier2);
    
    Dossier mergedDossier = storeOf(Dossier.class).find(String.valueOf(dossier.getId()));
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
    store.persist(null, dossier);
  }

  private static void modifyAccountHolder(Documents<Dossier> store, Dossier dossier)
  {
    AccountHolder accountHolder = dossier.accountHolder;
    accountHolder.bankBranch = "new Branch";
    store.persist(null, dossier);
  }
  
  @Test
  public void modifyConcurrentDifferentFieldsOnSameObject()
  {
    Dossier dossier = RandomDossier.generate();
    storeOf(Dossier.class).persist(null, dossier);
    
    Documents<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(String.valueOf(dossier.getId()));
    Documents<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(String.valueOf(dossier.getId()));
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    store1.persist(String.valueOf(dossier.getId()), loadedDossier);
    loadedDossier2.accountHolder.contactPerson = "Fritz";
    store2.persist(String.valueOf(dossier.getId()), loadedDossier2);
    
    Dossier mergedDossier = storeOf(Dossier.class).find(String.valueOf(dossier.getId()));
    assertThat(mergedDossier.accountHolder.bankBranch).isEqualTo(dossier.accountHolder.bankBranch);
    assertThat(mergedDossier.accountHolder.contactPerson).isEqualTo("Fritz");
    assertThat(mergedDossier.accountHolder.name).isEqualTo("Theo");
    // last wins one concurrent modification!
    
    
  }
  
  @Test
  public void modifyConcurrentDifferentFieldsOnSameObject_fail()
  {
    Dossier dossier = RandomDossier.generate();
    storeOf(Dossier.class).persist(null, dossier);
    
    Documents<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(String.valueOf(dossier.getId()));
    Documents<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(String.valueOf(dossier.getId()));
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    store1.persist("333", loadedDossier);
    try
    {
      loadedDossier2.accountHolder.contactPerson = "Fritz";
      store2.persist("333", loadedDossier2);
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
    storeOf(Dossier.class).persist(null, dossier);
    String dossierId = String.valueOf(dossier.getId());
    
    Documents<Dossier> store1 = storeOf(Dossier.class);
    Dossier loadedDossier = store1.find(dossierId);
    Documents<Dossier> store2 = storeOf(Dossier.class);
    Dossier loadedDossier2 = store2.find(dossierId);
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    store1.persist(dossierId, loadedDossier);
    try
    {
      loadedDossier2.accountHolder.name = "Fritz";
      store2.persist(dossierId, loadedDossier2);
      Assertions.failBecauseExceptionWasNotThrown(Throwable.class);
    }
    catch (Throwable th)
    {
      assertThat(th).hasMessageContaining("fail me!");
    }
  }
  
  
  private static <T> Documents<T> storeOf(Class<T> type)
  {
    Session session = Neo4jSessionFactory.getInstance().getFinSession();
    return new GraphDocs(session, type)
    {
      @Override
      public Object find(Long id)
      {
        int DEPTH = 3;
        return session.load(type, id, DEPTH);
      }
    };
  }
  
}
