package ch.ivyteam.fintech;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ch.ivyteam.java.object.store.AbstractRdbmsTest;
import ch.ivyteam.java.object.store.ObjectStore;
import ch.ivyteam.java.object.store.ObjectStore.Filters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance extends AbstractRdbmsTest
{

  private static final int DOSSIERS_PER_YEAR = 2_000_000;

  @Test
  public void t1_fillData()
  {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    
    ObjectStore<Dossier> repository = storeOf(Dossier.class);
    List<Dossier> randomDocs = new ArrayList<>();
    int safeAfter = 10_000;
    for(int i=0; i<DOSSIERS_PER_YEAR; i++)
    {
      randomDocs.add(RandomDossier.generate());
      if (i%safeAfter == 0)
      {
        repository.persist(randomDocs);
        randomDocs.clear();
        System.out.println("Saved "+safeAfter+" dossiers after "+ 
                TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())+" [sec]");
        stopWatch.reset();
        stopWatch.start();
      }
    }
  }
  
  @Test
  public void t2_readComplexDossier()
  {
    Dossier dossier = storeOf(Dossier.class).find(5l);
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
  }
  
  @Test
  public void t3_queryComplexDossier()
  {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    
    String searchName = "Aron";
    Collection<Dossier> dossiers = storeOf(Dossier.class)
            .query(new Filters()
                    .field("firstName", searchName)
                    .field("fiduciaryHoldingAssets", Boolean.FALSE.toString())
                  ).values();
    
    long dbTime = stopWatch.getTime();
    int removedHits = removeFalsePositives(searchName, dossiers);
    assertThat(removedHits).isGreaterThan(200); // many false positives...
    
    Dossier dossier = dossiers.iterator().next();
    assertThat(dossier).isNotNull();
    assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
    
    stopWatch.stop();
    System.out.println("Found "+dossiers.size()+" exact matches after "+seconds(stopWatch.getTime()) +"[s] "
            + "(db="+seconds(dbTime)+"[s], "
             + "javaFilter="+(stopWatch.getTime()-dbTime)+"[ms])");
    System.out.println("Filtered "+removedHits+" false positives");
    // for 100'000 records (+fullTextIndex) ~ 74 seconds
    //   2'777'321 records (+fullTextIndex) ~ 233 seconds
  }

  private static long seconds(long millis)
  {
    return TimeUnit.MILLISECONDS.toSeconds(millis);
  }
  
  private static int removeFalsePositives(String searchName, Collection<Dossier> dossiers)
  {
    int filtered = 0;
    for(Iterator<Dossier> it = dossiers.iterator(); it.hasNext();)
    {
      Dossier dossier = it.next();
      if (!containsBo(searchName, dossier))
      {
        filtered++;
        it.remove();
      }
    }
    return filtered;
  }

  private static boolean containsBo(String searchName, Dossier dossier)
  {
    for(BeneficialOwner beneficial : dossier.beneficialOwners)
    {
      if (beneficial.person.firstName.equals(searchName))
      {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void tearDown()
  {
    // do not drop schema! (it has taken so much time to create the data)
    super.closeConnection();
  }
  
}
