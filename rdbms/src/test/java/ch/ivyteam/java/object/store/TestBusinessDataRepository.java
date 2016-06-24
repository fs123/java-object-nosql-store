package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import ch.ivyteam.fintech.AccountHolder;
import ch.ivyteam.fintech.BeneficialOwner;
import ch.ivyteam.fintech.ControllingPerson;
import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.Person;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData;
<<<<<<< HEAD
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData.Updater;
import ch.ivyteam.java.object.store.memory.MemoryBusinessDataRepository;
=======
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessDataRepositoryExtended.Updater;
>>>>>>> stash

public class TestBusinessDataRepository
{

  private MemoryBusinessDataRepository repository;

  @Test
  public void create()
  {
    Dossier dossier = RandomDossier.generate();
    BusinessData<Dossier> businessData = repository.create(dossier);
    
    AccountHolder holder = new AccountHolder();
    holder.name = "Mr. Cool";
    businessData.object().accountHolder = holder;
    businessData.save();
  }
  
  @Test
  public void loadAndDestroy()
  {
    BusinessData<Dossier> dossier = repository.create(RandomDossier.generate());
    dossier.save();
    assertThat(repository.exists(dossier.getMeta().getId())).isTrue();    
    
    BusinessData<Dossier> bd = repository.find(dossier.getMeta().getId());
    bd.delete();
    assertThat(repository.exists(dossier.getMeta().getId())).isFalse();
  }
  
  @Test
  public void query()
  {
    Dossier myDossier = RandomDossier.generate();
    BeneficialOwner bo = new BeneficialOwner();
    bo.person = new Person();
    bo.person.firstName = "Aron";
    myDossier.beneficialOwners.add(bo);
    repository.create(myDossier).save();
    
    Dossier aronsDossier = repository.query(Dossier.class)
              .field("firstName").isEqualTo("Aron")
              .execute().objects().get(0);
    
    BeneficialOwner firstBOwner = aronsDossier.beneficialOwners.iterator().next();
    assertThat(firstBOwner.person.firstName).isEqualTo("Aron");
  }
  
  @Test
  public void updateConcurrent() throws InterruptedException
  {
    BusinessData<Dossier> businessData = repository.create(RandomDossier.generate());
    businessData.save();

    Thread t1 = new Thread(() -> {businessData.lockAndUpdate(deleteBeneficians());});
    Thread t2 = new Thread(() -> {businessData.lockAndUpdate(modifyCpm());});
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    
    BusinessData<Dossier> persistentData = repository.find(businessData.getMeta().getId());
    Dossier dossier = persistentData.object();
    assertThat(dossier.accountHolder.name).isEqualTo("The new Boss");
    assertThat(dossier.beneficialOwners).isEmpty();
  }
  
  private static Updater<Dossier> modifyCpm()
  {
    return dossier -> {
      dossier.controllingPersonMgmt.fiduciaryHoldingAssets = true;
      ControllingPerson controller = new ControllingPerson();
      controller.person = new Person();
      controller.person.firstName = "The new Boss";
      dossier.controllingPersonMgmt.controllingPersons.add(controller);
    };
  }

  private static Updater<Dossier> deleteBeneficians()
  {
    return dossier -> {dossier.beneficialOwners.clear();};
  }
  
  @Test
  public void metaData()
  {
    BusinessData<Dossier> businessData = repository.create(RandomDossier.generate());
    businessData.save();
    
    // work in other thread....
    BusinessData<Dossier> businessData2 = repository.find(businessData.getMeta().getId());
    businessData2.object().accountHolder.name = "Ooops I'm new";
    businessData2.save();
    // end work of other
    
    assertThat(businessData.isUpToDate()).isFalse();
    if (!businessData.isUpToDate())
    {
      businessData.reload();
      businessData.object().legitimateMgmt.onlyLegitimatesAreBO = true;
      businessData.save(); // FAILS if still not up to date - other concurrent update
      businessData.overwrite(); // writes my state over existing version ignoring out of sync.
    }
  }
  

  @Before
  public void setUp()
  {
    repository = new MemoryBusinessDataRepository();
  }
  
}
