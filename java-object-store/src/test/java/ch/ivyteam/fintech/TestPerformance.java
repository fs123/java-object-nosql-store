package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.mysql.JdbcDocumentPersistency;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance
{
  private JdbcDocumentPersistency persistency;
  private Documents<Dossier> documents;
  private static final int DOSSIERS_PER_YEAR = 2_000_000;

  @Before 
  public void before()
  {
	  persistency = new JdbcDocumentPersistency();
	  documents = persistency.get(Dossier.class, "jdbc:mysql://localhost:3306/nosql?user=root&password=nimda&useSSL=false");
  }
  @Test
  @Ignore
  public void t1_fillData()
  {
    for(int i=0; i<DOSSIERS_PER_YEAR; i++)
    {
      Dossier dossier = RandomDossier.generate();
      documents.persist(""+i, dossier);
      if (i%1000==0)
      {
    	  System.out.println(i);
      }
    }
  }
  
  @Test
  public void t2_readComplexDossier()
  {
    Dossier dossier = documents.find("1234");
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  @Test
  public void t3_queryComplexDossier()
  {
	Collection<Dossier> dossiers = documents.query("JSON_EXTRACT(DossierJson, '$.beneficialOwners[*].person.firstName') LIKE '%\"Aaron\"%' AND JSON_EXTRACT(DossierJson, '$.controllingPersonMgmt.fiduciaryHoldingAssets')=false");
//    Dossier dossier = new Dossier(); // query me from store
//    // where beneficiation firstname = "Aaron"
//    // and controllingPersonMgmt.fiduciarayHolssingAssets = true
    
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
}
