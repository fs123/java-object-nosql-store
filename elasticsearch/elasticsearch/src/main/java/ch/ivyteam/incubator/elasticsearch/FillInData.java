package ch.ivyteam.incubator.elasticsearch;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.fintech.Dossier;
import ch.ivyteam.fintech.RandomDossier;

public class FillInData {
	private static final int DOSSIERS_PER_YEAR = 2_000_000;

	public static void main(String[] args) throws Exception {


		long startMillis = System.currentTimeMillis();

		try (Client client = ElasticUtils.openClient()) {

			BulkRequestBuilder bulkRequest = client.prepareBulk();

			for (int i = 0; i <= DOSSIERS_PER_YEAR; i++) {

				Dossier dossier = RandomDossier.generate();
				ObjectMapper mapper = ElasticUtils.getMapper();
				byte[] source = mapper.writeValueAsBytes(dossier);
				bulkRequest.add(client.prepareIndex("dossier", "dossier").setSource(source));


				if (i % 10_000 == 0) {
					bulkRequest.get();
					bulkRequest = client.prepareBulk();

					long duration = System.currentTimeMillis() - startMillis;
					System.out.println("Generated and saved next 10'000 (needed: " + duration + "ms)");
				}
			}
		}
	}

}
