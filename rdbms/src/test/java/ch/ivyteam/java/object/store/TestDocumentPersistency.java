package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class TestDocumentPersistency
{

  @Test
  @Ignore("to be implemented")
  public void storeAndLoad()
  {
    DocumentPersistency persistency = new DocumentPersistency()
      {
        @Override
        public <T> Documents<T> get(Class<T> type, String config)
        {
          throw new RuntimeException("implement me!");
        }
      };
    Documents<Dossier> docStore = persistency.get(Dossier.class, "http://nosql/rest");
   
    Dossier doc = new Dossier();
    docStore.persist("1", doc);
    Dossier loadedDoc = docStore.find("1");
    
    assertThat(doc).isEqualTo(loadedDoc);
  }
  
  private static class Dossier
  {
    private String name = "Hello";
  }
  
}
