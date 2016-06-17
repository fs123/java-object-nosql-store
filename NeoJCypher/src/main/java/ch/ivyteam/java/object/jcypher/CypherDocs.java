package ch.ivyteam.java.object.jcypher;

import java.util.Collection;
import java.util.Collections;

import ch.ivyteam.java.object.store.Documents;
import iot.jcypher.domain.IDomainAccess;

public class CypherDocs<T> implements Documents<T>
{
  public final IDomainAccess domain;
  private Class<T> type;

  public CypherDocs(IDomainAccess domain, Class<T> type)
  {
    this.domain = domain;
    this.type = type;
  }

  @Override
  public void persist(String key, T obj)
  {
    domain.store(obj);
  }

  @Override
  public T find(String key)
  {
    return domain.loadById(type, 3, Long.valueOf(key));
  }

  @Override
  public Collection<T> findAll()
  {
    return Collections.emptyList();
  }

  @Override
  public void remove(String key)
  {
   // domain.
  }

  @Override
  public boolean exists(String key)
  {
    return false;
  }

  @Override
  public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filters)
  {
    return null;
  }
  
}
