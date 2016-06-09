package ch.ivyteam.java.object.store;

import java.util.Collection;
import java.util.Map.Entry;

import org.neo4j.ogm.session.Session;

import ch.ivyteam.neo4j.GenericService;
import ch.ivyteam.neo4j.domain.Entity;

public class GraphDocs<T extends Entity> extends GenericService<T> implements Documents<T>
{
  public GraphDocs(Session session, Class<T> type)
  {
    super(session, type);
  }
  
  @Override
  public T find(String key)
  {
    return find(Long.valueOf(key));
  }
  
  @Override
  public Collection<T> findAll()
  {
    return getAll();
  }
  
  @Override
  public void persist(String key, T obj)
  {
    persist(obj);
  }
  
  @Override
  public void remove(String key)
  {
    delete(Long.valueOf(key));
  }

  @Override
  public boolean exists(String key)
  {
    return find(key) != null;
  }

  @Override
  public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filters)
  {
    StringBuilder cypher = new StringBuilder();
    cypher.append("MATCH (t:"+getEntityType().getSimpleName()+" "+toJsonMap(filters)+")");
    cypher.append("\n").append("RETURN t");
    return query(cypher.toString());
  }

  private String toJsonMap(ch.ivyteam.java.object.store.Documents.Filters filters)
  {
    if (filters.isEmpty())
    {
      return "";
    }
    
    StringBuilder map = new StringBuilder();
    map.append("{");
    boolean first = true;
    for(Entry<String, String> filter : filters.getFieldFilters())
    {
      if (!first)
      {
        map.append(",");
      }
      map.append(filter.getKey()+":\""+filter.getValue()+"\"");
      first = false;
    }
    map.append("}");
    return map.toString();
  }


}
