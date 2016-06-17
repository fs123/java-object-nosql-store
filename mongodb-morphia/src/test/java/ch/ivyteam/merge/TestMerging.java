package ch.ivyteam.merge;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoClient;

import ch.ivyteam.fintech.AccountHolder;
import ch.ivyteam.fintech.BeneficialOwner;
import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.Person;
import ch.ivyteam.fintech.RandomDossier;

public class TestMerging
{

	private static Datastore datastore;
	
  @BeforeClass
  public static void init() throws InterruptedException {
    final Morphia morphia = new Morphia();
    datastore = morphia.createDatastore(new MongoClient(), "fintech");
    datastore.ensureIndexes();
  	datastore.getDB().dropDatabase();
  }
  
  @Test
  public void testIndependentChanges()
  {
    Dossier dossier = RandomDossier.generate();
    dossier.accountHolder.bankBranch = "first branch";
    
    datastore.save(dossier);
    
    Dossier loadedDossier = datastore.get(Dossier.class, dossier.id);
    Dossier loadedDossier2 = datastore.get(Dossier.class, dossier.id);
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    modifyAccountHolder(loadedDossier);
    modifyBeneficians(loadedDossier2);
    
    Dossier mergedDossier = datastore.get(Dossier.class, dossier.id);
    assertThat(mergedDossier).isNotSameAs(loadedDossier).isNotSameAs(loadedDossier2);
    
    assertThat(mergedDossier.accountHolder.bankBranch).isEqualTo("new Branch");
    assertThat(mergedDossier.accountHolder.dateOfRegistry).isEqualTo(dossier.accountHolder.dateOfRegistry);
    
    Person firstBenefician = mergedDossier.beneficialOwners.iterator().next().person;
    assertThat(firstBenefician.lastName).isEqualTo("New Married Name");
    assertThat(firstBenefician.firstName).isEqualTo(dossier.beneficialOwners.iterator().next().person.firstName);
  }

  private static void modifyBeneficians(Dossier dossier)
  {
    BeneficialOwner firstBenefician = dossier.beneficialOwners.iterator().next();
    firstBenefician.person.lastName = "New Married Name";
    
    // I haven't found a clean/simple way to update only the first element of the array, 
    // but there are several extended functionalities: 
    // https://github.com/mongodb/morphia/wiki/Updating
    UpdateOperations<Dossier> updateOperations = datastore
    		.createUpdateOperations(Dossier.class)
    		.set("beneficialOwners", dossier.beneficialOwners);
    // the given dossier reference is up out-dated, therefore we have to create a specific update-query.
    Query<Dossier> query = datastore.createQuery(Dossier.class)
    		.filter("id", dossier.id);
    		// Compare and Swap easy done with...:
    		//.filter("beneficialOwners", oldbeneficialOwners);
    UpdateResults updateResults = datastore.update(query, updateOperations);
    assertThat(updateResults.getUpdatedCount()).isEqualTo(1);
  }

  private static void modifyAccountHolder(Dossier dossier)
  {
    AccountHolder accountHolder = dossier.accountHolder;
    accountHolder.bankBranch = "new Branch";
    UpdateOperations<Dossier> updateOperations = datastore
    		.createUpdateOperations(Dossier.class)
    		.set("accountHolder", accountHolder);
    // the given dossier reference is up to date
    datastore.update(dossier, updateOperations);
  }
  
  /**
   * Could be solved like the above test {@link #testIndependentChanges()}()
   */
  @Test @Ignore
  public void modifyConcurrentDifferentFieldsOnSameObject()
  {
    Dossier dossier = RandomDossier.generate();
    
    datastore.save(dossier);
    
    Dossier loadedDossier = datastore.get(Dossier.class, dossier.id);
    Dossier loadedDossier2 = datastore.get(Dossier.class, dossier.id);
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    datastore.save(loadedDossier);
    loadedDossier2.accountHolder.contactPerson = "Fritz";
    datastore.save(loadedDossier2);
    
    Dossier mergedDossier = datastore.get(Dossier.class, dossier.id);
    assertThat(mergedDossier.accountHolder.name).isEqualTo("Theo");
    assertThat(mergedDossier.accountHolder.contactPerson).isEqualTo("Fritz");
    assertThat(mergedDossier.accountHolder.bankBranch).isEqualTo(dossier.accountHolder.bankBranch);
  }
  
  @Test
  public void modifyConcurrentDifferentFieldsOnSameObject_fail()
  {
    Dossier dossier = RandomDossier.generate();
    datastore.save(dossier);
    
    Dossier loadedDossier = datastore.get(Dossier.class, dossier.id);
    Dossier loadedDossier2 = datastore.get(Dossier.class, dossier.id);
   
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
//    loadedDossier.accountHolder.name = "Theo";+
    // This will increment the field annotated with @version 
    datastore.save(loadedDossier);
    try
    {
    	// Note: we update the full dossier. We can't set a @version on a embedded object
      loadedDossier2.accountHolder.contactPerson = "Fritz";
      datastore.save(loadedDossier2);
      Assertions.failBecauseExceptionWasNotThrown(Throwable.class);
    }
    catch (Throwable th)
    {
      assertThat(th).hasMessageContaining("was concurrently updated");
    }
  }
  
  @Test
  public void modifyConcurrentSameField()
  {
    Dossier dossier = RandomDossier.generate();
    datastore.save(dossier);
    
    Dossier loadedDossier = datastore.get(Dossier.class, dossier.id);
    Dossier loadedDossier2 = datastore.get(Dossier.class, dossier.id);
   
    assertThat(loadedDossier).isNotSameAs(loadedDossier2);
    assertThat(loadedDossier).isEqualToComparingFieldByFieldRecursively(loadedDossier2);
    
    loadedDossier.accountHolder.name = "Theo";
    datastore.save(loadedDossier);
    try
    {
      loadedDossier2.accountHolder.name = "Fritz";
      datastore.save(loadedDossier2);
      Assertions.failBecauseExceptionWasNotThrown(Throwable.class);
    }
    catch (Throwable th)
    {
    	// throws, because of the @Version annotation on the Dossier
      assertThat(th).hasMessageContaining("was concurrently updated");
    }
  }
}
