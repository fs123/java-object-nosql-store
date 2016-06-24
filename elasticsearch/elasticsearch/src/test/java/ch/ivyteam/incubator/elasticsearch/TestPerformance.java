package ch.ivyteam.incubator.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.fintech.Dossier;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestPerformance {

	@Test
	public void t1_readComplexDossier() throws Exception {
		try (Client client = ElasticUtils.openClient()) {
			GetResponse response = client.prepareGet("dossier", "dossier", "AVV8xBJcNBBynKYB23Td").get();

			ObjectMapper mapper = ElasticUtils.getMapper();
			Dossier dossier = mapper.readValue(response.getSourceAsBytes(), Dossier.class);

			assertThat(dossier).isNotNull();
			assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
		}
	}

	@Test
	public void t2_queryFullText() throws Exception {
		executeQuery(QueryBuilders.queryStringQuery("Hermine"));
	}

	@Test
	public void t3_queryComplex() throws Exception {
		executeQuery(
				QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("beneficialOwners.person.firstName", "Hermine"))
						.must(QueryBuilders.matchQuery("controllingPersonMgmt.fiduciaryHoldingAssets", "false")));
	}

	@Test
	public void t4_queryComplexWithRange() throws Exception {
		executeQuery(QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery("beneficialOwners.person.firstName", "Hermine"))
				.must(QueryBuilders.matchQuery("controllingPersonMgmt.fiduciaryHoldingAssets", "false"))
				.must(QueryBuilders.rangeQuery("accountHolder.dateOfRegistry").from("2013-06-08").to("2013-08-08")));
	}

	@Test
	public void t5_queryWithRange() throws Exception {
		executeQuery(QueryBuilders.rangeQuery("accountHolder.dateOfRegistry").from("2013-07-08").to("2013-08-08"));
	}

	@Test
	public void t6_queryWithMultipleRange() throws Exception {
		executeQuery(QueryBuilders.boolQuery()
				.must(QueryBuilders.rangeQuery("beneficialOwners.person.birthDate").from("2013-07-08").to("2013-08-08"))
				.must(QueryBuilders.rangeQuery("accountHolder.dateOfRegistry").from("2013-07-08").to("2013-08-08"))
				.must(QueryBuilders.rangeQuery("accountHolder.numberOfEmployees").from(75832).to(88581)));

	}

	private static void executeQuery(QueryBuilder query) throws Exception {
		try (Client client = ElasticUtils.openClient()) {
			long startMillis = System.currentTimeMillis();

			SearchResponse response = client.prepareSearch().setQuery(query).setSize(5).execute().actionGet();

			SearchHits hits = response.getHits();
			System.out.println("Query: " + query.toString());
			System.out.println("Duration: " + (System.currentTimeMillis() - startMillis) + "ms");

			writeHits(hits);
		}
	}

	private static void writeHits(SearchHits hits) throws Exception {
		System.out.println("Hits: " + hits.getTotalHits());

		ObjectMapper mapper = ElasticUtils.getMapper();

		for (SearchHit hit : hits.getHits()) {
			BytesReference sourceRef = hit.getSourceRef();
			Dossier dossier = mapper.readValue(sourceRef.toBytes(), Dossier.class);
			System.out.println(hit.getId() + ":" + mapper.writeValueAsString(dossier));
			assertThat(dossier).isNotNull();
			assertThat(dossier.beneficialOwners.iterator().next().person.firstName).isNotNull();
		}
	}

}
