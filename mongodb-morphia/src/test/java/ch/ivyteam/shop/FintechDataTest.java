package ch.ivyteam.shop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;

import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.RandomDossier;

public class FintechDataTest {
	
	public static boolean recreateDossiers = false;
	
	private static final String DOSSIERS = "dossiers";

	private static final int DOSSIERS_PER_YEAR = 2_000_000;
	
	private Datastore datastore;
	
  @Before
  public void init() throws InterruptedException {
    final Morphia morphia = new Morphia();
    morphia.mapPackage("ch.ivyteam.fintech");
    datastore = morphia.createDatastore(new MongoClient(), "fintech");
    datastore.ensureIndexes();
    if (recreateDossiers) {
    	recreateDossiers = false;
    	datastore.getDB().dropDatabase();
    }
    if (datastore.getDB().getCollection(DOSSIERS).count() == 0) {
    	fillData();
    }
  }
  
  public void fillData() throws InterruptedException {
  	
  	ExecutorService executor = Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors()-1);
  	
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		System.out.print("Create and store "+ DOSSIERS_PER_YEAR + " dossiers");
		for (int i = 0; i < DOSSIERS_PER_YEAR; i++) {
			if (i%20000 == 0) {
				System.out.println("> " + i);
			}
			executor.execute(() -> {
				datastore.save(RandomDossier.generate());
			});
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		
		stopWatch.stop();
		
		long saveTime = stopWatch.getTime();
		long dossiersPerSeconds = DOSSIERS_PER_YEAR / (saveTime/1000);
		System.out.println("Created " + DOSSIERS_PER_YEAR + " dossierts in " + saveTime + "ms => "+ dossiersPerSeconds  +"/s with " + (Runtime.getRuntime().availableProcessors()-1) + " threads");
		
		// SINGLE THREAD: > Created 2000000 dossierts in 833700ms (14min)=> 2400/s
		// 7 THREAD     : > Created 2000000 dossierts in 299244ms ( 5min)=> 6688/s with 7 threads
	}
	
	@Test @Ignore
	public void t2_readComplexDossier() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		List<Dossier> dossiers = datastore
				.createQuery(Dossier.class)
				.offset(123_456)
				.limit(2)
				.asList();

		// > Found 2 dossiers in 43ms/47ms/44ms/45ms/52ms (without indexs)
		System.out.println("Found " + dossiers.size() + " dossiers in "+ stopWatch.getTime() + "ms");
		
		assertThat(dossiers).hasSize(2);
		assertThat(dossiers.get(0).beneficialOwners.iterator().next().person.firstName).isNotNull();
	}
	
	@Test
	public void t3_queryComplexDossierIndexOnEachField() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Query<Dossier> filter = datastore
			.createQuery(Dossier.class)
			.filter("beneficialOwners.person.lastName", "Keesler") // A (= 71 documents)
			.filter("legitimateMgmt.onlyLegitimatesAreBO", true)   // B (too many to query)
			.filter("controllingPersonMgmt.controllingPersons.share >= ", 50) // C
		;
		 
		List<Dossier> dossiers = filter.asList();
		
		stopWatch.stop();
		
		// Without index on A,B,C > Found 25 dossiers in 5295ms/5394ms/5266ms/5346ms/5244ms
		// With    index on A     > Found 25 dossiers in 20ms/13ms/106ms/105ms
		System.out.println("Found " + dossiers.size() + " dossiers in "+ stopWatch.getTime() + "ms [Keesler]");
		
		if (dossiers.isEmpty()) {
			return;
		}
		
		System.out.println("First dossier: ");
		System.out.println(ToStringBuilder.reflectionToString(dossiers.get(0)));
	}
	
	@Test
	public void t3_queryComplexDossierSingleIndex() {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Query<Dossier> filter = datastore
			.createQuery(Dossier.class)
			.filter("beneficialOwners.person.firstName", "Catina")
			.filter("controllingPersonMgmt.fiduciaryHoldingAssets", false)
			.filter("controllingPersonMgmt.controllingPersons.share >= ", 50);
		
//		List<Dossier> dossiers = filter.asList();
		long countAll = filter.countAll();
		stopWatch.stop();
		// > Found 660 dossiers in 13590ms/9437ms/9273ms/9476ms/9352ms (without index)
		// > Found 660 dossiers in 764ms/661ms/669ms/616ms (with one index which includes the two first fields of the query)
		// > Found 660 dossiers in 24ms (just query to count of result, without creating the dossiers) 
		System.out.println("Found " + countAll + " dossiers in "+ stopWatch.getTime() + "ms [Catina]");
		
//		if (dossiers.isEmpty()) {
//			return;
//		}
//		
//		System.out.println("First dossier: ");
//		System.out.println(ToStringBuilder.reflectionToString(dossiers.get(0)));
	}
}
