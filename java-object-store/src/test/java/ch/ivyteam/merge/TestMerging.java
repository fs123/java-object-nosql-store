package ch.ivyteam.merge;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import ch.ivyteam.fintech.AccountHolder;
import ch.ivyteam.fintech.BeneficialOwner;
import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.Person;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.MemoryDocumentPersistency;

public class TestMerging
{

  @Test
  public void testIndependentChanges()
  {
    Dossier dossier = RandomDossier.generate();
    dossier.accountHolder.bankBranch = "first branch";
    Documents<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist("111", dossier);
    
    Dossier loadedDossier = dossierStore.find("111");
    Dossier loadedDossier2 = dossierStore.find("111");
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    modifyAccountHolder(dossierStore, loadedDossier);
    modifyBeneficians(dossierStore, loadedDossier2);
    
    Dossier mergedDossier = dossierStore.find("111");
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
    Documents<Dossier> dossierStore = storeOf(Dossier.class);
    dossierStore.persist("222", dossier);
    
    Dossier loadedDossier = dossierStore.find("222");
    Dossier loadedDossier2 = dossierStore.find("222");
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    dossierStore.persist("222", loadedDossier);
    loadedDossier2.accountHolder.contactPerson = "Fritz";
    dossierStore.persist("222", loadedDossier2);
    
    Dossier mergedDossier = dossierStore.find("222");
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
  
  
  private static <T> Documents<T> storeOf(Class<T> type)
  {
    // switch to your real implementation!!!
    return new MemoryDocumentPersistency().get(type, null);
  }
  
}
