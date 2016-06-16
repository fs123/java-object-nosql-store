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
    Dossier dossier = documents.find("1234070");
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  @Test
  // Seems not to work: Create Index: CREATE INDEX idxBenOwn ON Dossier USING gin ((DossierJson -> 'beneficialOwners'));
  // Seems not to work: Create Index: CREATE INDEX idxBenOwnFN2 ON Dossier USING gin ((DossierJson -> 'beneficialOwners' -> 'person' -> 'firstName'));
  // Seems to work: CREATE INDEX idxgin ON Dossier USING gin (DossierJson);
  public void t3_queryComplexDossier()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"beneficialOwners\" : [{\"person\": {\"firstName\": \"Aaron\"}}]}'::jsonb AND DossierJson->'controllingPersonMgmt'->'fiduciaryHoldingAssets' = 'false'::jsonb");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  
  @Test
  public void t4_queryComplexDossierOnlyFirstname()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"beneficialOwners\" : [{\"person\": {\"firstName\": \"Aaron\"}}]}'::jsonb");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  @Test
  public void t5_queryComplexDossierBothNames()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"beneficialOwners\" : [{\"person\": {\"firstName\": \"Aaron\", \"lastName\": \"Trant\"}}]}'::jsonb AND DossierJson->'controllingPersonMgmt'->'fiduciaryHoldingAssets' = 'false'::jsonb");
	assertThat(dossiers.size()).isEqualTo(1);
  }
  
  @Test
  public void t6_queryComplexDossierForZipCodeSlow()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson->'accountHolder'->'address'->>'zipCode' = '39110'");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  @Test
  public void t7_queryComplexDossierForZipCodeFaster()
  {
	Collection<Dossier> dossiers = documents.query("DossierJson @> '{\"accountHolder\" : {\"address\": {\"zipCode\": \"39110\"}}}'::jsonb");
	assertThat(dossiers.size()).isGreaterThanOrEqualTo(1);
  }
  
  
}
