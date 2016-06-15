package ch.ivyteam.shop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

import ch.ivyteam.fintech.DossierWithOnlyFullIndex;
import ch.ivyteam.fintech.RandomDossierWithOnlyFullIndex;

public class FulltextIndexTest {
	
	private static boolean recreateDossierWithOnlyFullIndexs = false;
	private static final String DossierWithOnlyFullIndexS = "DossiersWithOnlyFullIndex";
	private static final int DossierWithOnlyFullIndexS_PER_YEAR = 2_000_000;
	
	private Datastore datastore;
	
  @Before
  public void init() throws InterruptedException {
    final Morphia morphia = new Morphia();
    morphia.mapPackage("ch.ivyteam.fintech");
    datastore = morphia.createDatastore(new MongoClient(), "fintech");
    datastore.ensureIndexes();
    if (recreateDossierWithOnlyFullIndexs) {
    	recreateDossierWithOnlyFullIndexs = false;
    	datastore.getDB().dropDatabase();
    }
    if (datastore.getDB().getCollection(DossierWithOnlyFullIndexS).count() == 0) {
    	fillData();
    }
  }
  
  public void fillData() throws InterruptedException {
  	ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors()-1);
  	
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		System.out.print("Create and store "+ DossierWithOnlyFullIndexS_PER_YEAR + " DossierWithOnlyFullIndexs");
		for (int i = 0; i < DossierWithOnlyFullIndexS_PER_YEAR; i++) {
			if (i%20000 == 0) {
				System.out.println("> " + i);
			}
			executor.execute(() -> {
				datastore.save(RandomDossierWithOnlyFullIndex.generate());
			});
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		
		stopWatch.stop();
		
		long saveTime = stopWatch.getTime();
		long DossierWithOnlyFullIndexsPerSeconds = DossierWithOnlyFullIndexS_PER_YEAR / (saveTime/1000);
		System.out.println("Created " + DossierWithOnlyFullIndexS_PER_YEAR + " DossierWithOnlyFullIndexts in " + saveTime + "ms => "+ DossierWithOnlyFullIndexsPerSeconds  +"/s with " + (Runtime.getRuntime().availableProcessors()-1) + " threads");
	}
	
	@Test
	public void t3_queryComplexDossierSingleIndex() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Query<DossierWithOnlyFullIndex> filter = datastore
			.createQuery(DossierWithOnlyFullIndex.class)
			.filter("legitimateMgmt.legitimates.person.firstName", "Thea")
			.filter("controllingPersonMgmt.controllingPersons.address.country", "France");
		
		long countAll = filter.countAll();
		stopWatch.stop();
		// > Found 335 dossiers in 101357ms (with text index)
		System.out.println("Found " + countAll + " dossiers in "+ stopWatch.getTime() + "ms [Catina]");
	}
	
	@Test
	public void t3_queryComplexDossierWithOnlyFullIndexWithRegexQuery() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		// SEE: http://mongodb.github.io/morphia/1.0/guides/querying/
		Query<DossierWithOnlyFullIndex> filter = datastore
			.createQuery(DossierWithOnlyFullIndex.class)
			.search("Catina Trimble Yoshie Norseworthy");
		long countAll = filter.countAll();
		stopWatch.stop();
		// > Found 5769 DossierWithOnlyFullIndexs in 64ms/50ms/49ms/41ms (with fulltext index)
		System.out.println("Searched " + countAll + " DossierWithOnlyFullIndexs in "+ stopWatch.getTime() + "ms");
	}
}
