package ch.ivyteam.fintech.neo4j;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.neo4j.ogm.session.Session;

import ch.ivyteam.neo4j.Neo4jSessionFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance
{
  private static final int DOSSIERS_PER_YEAR = 200_000;

  @Test
  public void t1_fillData()
  {
    Session session = Neo4jSessionFactory.getInstance().getFinSession();

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    double percentageDone = 0;
    
    int dossiers = DOSSIERS_PER_YEAR*10;
    for(int i=0; i<dossiers; i++)
    {
      Dossier generated = RandomDossier.generate();
      session.save(generated);
      session.detachNodeEntity(generated.getId()); // relax
      
      double newPercentageDone = ((double)(i+1)/dossiers)*100;
      if (Math.floor(newPercentageDone) > percentageDone)
      {
        percentageDone = Math.floor(newPercentageDone);
        session.clear(); // let my memory chill
        System.out.println("filled "+percentageDone+"% of data in "+stopWatch.getTime()+" ms");
      }
    }
  }
  
  @Test
  public void t2_readComplexDossier()
  {
    Dossier dossier = loadFully(357);
    
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName)
      .as("can load full object tree").isNotNull();
  }

  private static Dossier loadFully(long id)
  {
    Session session = Neo4jSessionFactory.getInstance().getFinSession();
    int DEPTH = 3;
    return session.load(Dossier.class, id, DEPTH);
  }
  
  @Test
  public void t3_queryComplexDossier()
  {
    String cypher = "MATCH (:Person {firstName:\"Maybelle\"})<-[:BENEFIT_BY]-(:BeneficialOwner)<-[:BENEFITS]-(d:Dossier)"
                          + "-[:CONTROLLED_TROUGH]->(:ControllingPersonManagement {fiduciaryHoldingAssets:false}) "
            + "RETURN d";
    
    Session session = Neo4jSessionFactory.getInstance().getFinSession();
    Iterable<Map<String, Object>> result = session.query(cypher, Collections.emptyMap()).queryResults();
    assertThat(result).isNotEmpty();
    
    Dossier d = (Dossier) result.iterator().next().get("d");
    Dossier dossier = loadFully(d.getId());
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName)
      .as("can load full object tree").isNotNull();
  }
  
}
