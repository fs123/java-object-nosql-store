package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.postgres.JdbcDocumentPersistency;
import ch.ivyteam.test.postgres.PostgresCon;

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
	  documents = persistency.get(Dossier.class, PostgresCon.connectionString);
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
    Dossier dossier = documents.find("1234000");
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  @Test
  public void t3_queryComplexDossier()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"beneficialOwners\" : [{\"person\": {\"firstName\": \"Aaron\"}}]}'::jsonb AND DossierJson->'controllingPersonMgmt'->'fiduciaryHoldingAssets' = 'false'::jsonb");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  
  @Test
  public void t3_queryComplexDossierOnlyFirstname()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"beneficialOwners\" : [{\"person\": {\"firstName\": \"Aaron\"}}]}'::jsonb");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  
  
  @Test
  public void t3_queryComplexDossierForZipCode()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson->'accountHolder'->'address'->>'zipCode' = '39110'");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
}
