package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.ivyteam.fintech.AccountHolder;
import ch.ivyteam.fintech.BeneficialOwner;
import ch.ivyteam.fintech.ControllingPerson;
import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.Person;
import ch.ivyteam.fintech.RandomDossier;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData.Updater;

public class TestBusinessDataRepository
{

  @Test
  public void create()
  {
    Dossier dossier = RandomDossier.generate();
    BusinessData<Dossier> businessData = repository().create(dossier);
    
    AccountHolder holder = new AccountHolder();
    holder.name = "Mr. Cool";
    businessData.object().accountHolder = holder;
    businessData.save();
  }
  
  @Test
  public void loadAndDestroy()
  {
    BusinessData<Dossier> bd = repository().find(123l);
    bd.delete();
  }
  
  @Test
  public void query()
  {
    Dossier aronsDossier = repository().query(Dossier.class)
              .field("firstName").isEqualTo("Aron")
              .execute().objects().get(0);
    
    BeneficialOwner firstBOwner = aronsDossier.beneficialOwners.iterator().next();
    assertThat(firstBOwner.person.firstName).isEqualTo("Aron");
  }
  
  @Test
  public void updateConcurrent()
  {
    BusinessData<Dossier> businessData = repository().find(456l);
    Runnable r1 = () -> {businessData.lockAndUpdate(deleteBeneficians());};
    Runnable r2 = () -> {businessData.lockAndUpdate(modifyCpm());};
    new Thread(r1).start();
    new Thread(r2).start();
    
    ch.ivyteam.fintech.Address newAddress = null;

    businessData.update(dossier -> dossier.accountHolder.address = newAddress);
    
    

    businessData.update(dossier -> dossier.accountHolder.numberOfEmployees++);
    
    
    BeneficialOwner newOwner = null;
    businessData.update(dossier -> dossier.beneficialOwners.add(newOwner));
    
    // dgauch: simplify it for ivy script users!
    //businessData.updateAndOverride("beneficialOwners.person.address"); 
    
    businessData.save();
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
    BusinessData<Dossier> businessData = repository().find(333l);
    
    // long work...
    
    if (!businessData.isUpToDate())
    {
      businessData.reload();
      businessData.object().legitimateMgmt.onlyLegitimatesAreBO = true;
      businessData.save(); // FAILS if still not up to date - other concurrent update
      businessData.overwrite(); // writes my state over existing version ignoring out of sync.
    }
  }
  

  public static BusinessDataRepository repository()
  {
    return new MemoryBusinessDataRepository();
  }
  
}
