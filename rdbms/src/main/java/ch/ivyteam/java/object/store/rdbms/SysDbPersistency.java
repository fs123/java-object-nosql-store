package ch.ivyteam.java.object.store.rdbms;

import ch.ivyteam.java.object.store.DocumentPersistency;
import ch.ivyteam.java.object.store.Documents;

public class SysDbPersistency implements DocumentPersistency
{

  @Override
  public <T> Documents<T> get(Class<T> type, String jdbcUrl)
  {
    return new SysDbStore<>(type, JdbcConnectionFactory.connect(jdbcUrl));
  }

}
