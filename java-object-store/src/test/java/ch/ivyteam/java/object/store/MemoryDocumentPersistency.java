package ch.ivyteam.java.object.store;

public class MemoryDocumentPersistency implements DocumentPersistency
{

  @Override
  public <T> Documents<T> get(Class<T> type, String config)
  {
    return new MemoryDocStore<T>();
  }

}
