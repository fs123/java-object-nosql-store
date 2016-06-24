package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;
import java.util.Collection;
import java.util.Map.Entry;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.ObjectStore;

public class SysDbStore<T> implements Documents<T>
{
  private final SysDbRepository<T> repository;

  public SysDbStore(Class<T> type, Connection connection)
  {
    repository = new SysDbRepository<>(type, connection);
  }

  @Override
  public void persist(String key, T obj)
  {
    if (key == null)
    {
      repository.persist(obj);
    }
    else
    {
      repository.merge(Long.valueOf(key), obj);
    }
  }

  @Override
  public T find(String key)
  {
    return repository.find(Long.valueOf(key));
  }

  @Override
  public Collection<T> findAll()
  {
    return repository.findAll().values();
  }

  @Override
  public void remove(String key)
  {
    repository.delete(Long.valueOf(key));
  }

  @Override
  public boolean exists(String key)
  {
    return repository.exists(Long.valueOf(key));
  }

  @Override
  public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filtersV1)
  {
    return repository.query(toV2(filtersV1)).values();
  }

  private static ObjectStore.Filters toV2(ch.ivyteam.java.object.store.Documents.Filters filtersV1)
  {
    ObjectStore.Filters filterV2 = new ObjectStore.Filters();
    for(Entry<String, String> filterV1 : filtersV1.getFieldFilters())
    {
      filterV2.field(filterV1.getKey(), filterV1.getValue());
    }
    return filterV2;
  }

}
