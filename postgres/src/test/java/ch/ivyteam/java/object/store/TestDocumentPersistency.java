package ch.ivyteam.java.object.store;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.Ignore;
import org.junit.Test;

import ch.ivyteam.java.object.store.postgres.JdbcDocumentPersistency;
import ch.ivyteam.test.postgres.PostgresCon;


public class TestDocumentPersistency
{

  @Test
  public void storeAndLoad()
  {
    DocumentPersistency persistency = new JdbcDocumentPersistency();
    Documents<Dossier> docStore = persistency.get(Dossier.class, PostgresCon.connectionString);
   
    Dossier doc = new Dossier();
    docStore.persist("1", doc);
    Dossier loadedDoc = docStore.find("1");
    
    assertThat(doc).isEqualTo(loadedDoc);
  }
  
  private static class Dossier
  {
    private String name = "Hello";
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    @Override
    public boolean equals(Object obj) 
    {
    	if (obj instanceof Dossier)
    	{
    		Dossier other = (Dossier)obj;
    		return Objects.equals(other.name, name);
    	}
    	return super.equals(obj);
    }
  }
  
}
