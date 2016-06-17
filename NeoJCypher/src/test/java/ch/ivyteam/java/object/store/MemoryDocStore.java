package ch.ivyteam.java.object.store;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ch.ivyteam.java.object.store.Documents;

class MemoryDocStore<T> implements Documents<T>
{
  Map<String, T> objects = new HashMap<>();
  
  @Override
  public void persist(String key, T obj)
  {
    objects.put(key, obj);
  }

  @Override
  public T find(String key)
  {
    return objects.get(key);
  }

  @Override
  public Collection<T> findAll()
  {
    return objects.values();
  }

  @Override
  public void remove(String key)
  {
    objects.remove(key);
  }

  @Override
  public boolean exists(String key)
  {
    return objects.containsKey(key);
  }

  @Override
  public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filters)
  {
    return Collections.emptyList();
  }
  
}