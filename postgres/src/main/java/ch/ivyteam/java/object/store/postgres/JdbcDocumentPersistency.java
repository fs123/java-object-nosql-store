package ch.ivyteam.java.object.store.postgres;

import ch.ivyteam.java.object.store.DocumentPersistency;
import ch.ivyteam.java.object.store.Documents;

public final class JdbcDocumentPersistency implements DocumentPersistency {
	@Override
	public <T> Documents<T> get(Class<T> type, String config)
	{
	  return new PostgresDocuments<T>(type, config);
	}
}