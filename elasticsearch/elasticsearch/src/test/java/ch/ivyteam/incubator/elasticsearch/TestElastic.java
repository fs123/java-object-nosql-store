package ch.ivyteam.incubator.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.RandomDossier;

public class TestElastic {

	@Test
	public void testName() throws Exception {

		try (Client client = createClient()) {
			Dossier dossier = RandomDossier.generate();
			ObjectMapper mapper = new ObjectMapper();
			byte[] source = mapper.writeValueAsBytes(dossier);
			IndexResponse indexResponse = client.prepareIndex("doc", "doc").setSource(source).get();

			Assertions.assertThat(indexResponse.isCreated()).isTrue();
			System.out.println("Id: " + indexResponse.getId());
			System.out.println("Index: " + indexResponse.getIndex());
			System.out.println("Version: " + indexResponse.getVersion());

			SearchResponse response = client.prepareSearch("dossier").setTypes("dossier").execute().actionGet();

			SearchHits hits = response.getHits();
			System.out.println("Total hits: " + hits.getTotalHits());

			for (SearchHit hit : response.getHits().getHits()) {

				System.out.println("Hit: " + hit.getSourceAsString());
				Map<String, SearchHitField> fields = hit.getFields();
				for (Map.Entry<String, SearchHitField> fieldEntry : fields.entrySet())
				{

					System.out.println(fieldEntry.getValue().getName() + ": " + fieldEntry.getValue().getValue());
				}
				for (SearchHitField searchHitField : hit) {

					System.out.println(searchHitField.getName() + ": " + searchHitField.getValue());
				}
			}
		}
	}

	private TransportClient createClient() throws UnknownHostException {
		return TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	}

}
