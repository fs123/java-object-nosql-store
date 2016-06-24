package ch.ivyteam.java.object.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.ivyteam.java.object.store.serialize.Serializer;

public class MemoryJsonStore<T> implements ObjectStore<T>
{
  public Map<Long, String> json = new HashMap<>();
  private Serializer<T> serializer;

  public MemoryJsonStore(Serializer<T> serializer)
  {
    this.serializer = serializer;
  }
  
  @Override
  public Long persist(T obj)
  {
    Long id = (long)json.size();
    json.put(id, serializer.serialize(obj));
    return id;
  }

  @Override
  public Set<Long> persist(List<T> obj)
  {
    return null;
  }

  @Override
  public void merge(Long key, T obj)
  {
    json.put(key, serializer.serialize(obj));
  }

  @Override
  public T find(Long key)
  {
    return serializer.deserialize(json.get(key));
  }

  @Override
  public Map<Long, T> findAll()
  {
    return null;
  }

  @Override
  public Map<Long, T> query(ch.ivyteam.java.object.store.ObjectStore.Filters filters)
  {
    return null;
  }

  @Override
  public boolean exists(Long key)
  {
    return json.containsKey(key);
  }

  @Override
  public void delete(Long key)
  {
    json.remove(key);
  }

}
