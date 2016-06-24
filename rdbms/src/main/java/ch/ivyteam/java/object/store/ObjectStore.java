package ch.ivyteam.java.object.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Draft of an object store for Business Data
 * 
 * @since 22.06.2016
 * @param <T>
 */
public interface ObjectStore<T>
{
  public Long persist(T obj);
  public Set<Long> persist(List<T> obj);
  public void merge(Long key, T obj);
  
  public T find(Long key);
  public Map<Long, T> findAll();
  public Map<Long, T> query(Filters filters); // make it fluent!
  public boolean exists(Long key);
  
  public void delete(Long key);
  
  public static class Filters
  {
    private final Map<String, String> filters = new HashMap<>();
    
    public Filters field(String fieldName, String value)
    {
      filters.put(fieldName, value);
      return this;
    }
    
    public Set<Entry<String, String>> getFieldFilters()
    {
      return filters.entrySet();
    }
    
    public boolean isEmpty()
    {
      return filters.isEmpty();
    }
  }
  
}
