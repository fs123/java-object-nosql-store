package ch.ivyteam.java.object.store;

import ch.ivyteam.neo4j.Neo4jSessionFactory;

public class GraphDocPersistency implements DocumentPersistency
{

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T> Documents<T> get(Class<T> type, String config)
  {
    return new GraphDocs(Neo4jSessionFactory.getInstance().getNeo4jSessionV1(), type);
  }

}
