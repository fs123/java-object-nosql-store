package ch.ivyteam.java.object.store.memory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.java.object.store.ObjectStore;

class MemoryObjectStore<T> implements ObjectStore<T>
{
  Map<Long, T> objects = new HashMap<>();

  @Override
  public Long persist(T obj)
  {
    long nextId = objects.size();
    merge(nextId, obj);
    return nextId;
  }

  @Override
  public Set<Long> persist(List<T> obj)
  {
    return null;
  }

  @Override
  public void merge(Long key, T obj)
  {
    objects.put(key, obj);
  }

  @Override
  public T find(Long key)
  {
    return objects.get(key);
  }

  @Override
  public Map<Long, T> findAll()
  {
    return Collections.unmodifiableMap(objects);
  }

  @Override
  public Map<Long, T> query(ch.ivyteam.java.object.store.ObjectStore.Filters filters)
  {
    return findAll();
  }

  @Override
  public boolean exists(Long key)
  {
    return objects.containsKey(key);
  }

  @Override
  public void delete(Long key)
  {
    objects.remove(key);
  }
  
}