package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance
{

  private static final int DOSSIERS_PER_YEAR = 2_000_000;

  @Test
  public void t1_fillData()
  {
    List<Dossier> randomDocs = new ArrayList<>();
    for(int i=0; i<DOSSIERS_PER_YEAR; i++)
    {
      randomDocs.add(RandomDossier.generate());
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
  
}
