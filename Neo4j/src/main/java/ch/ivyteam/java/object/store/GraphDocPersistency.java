package ch.ivyteam.java.object.store;

public class GraphDocPersistency implements DocumentPersistency
{

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T> Documents<T> get(Class<T> type, String config)
  {
    return new GraphDocs(type);
  }

}
