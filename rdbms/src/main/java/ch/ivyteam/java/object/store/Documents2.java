package ch.ivyteam.java.object.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Enhanced object store Business Data
 * 
 * @since 22.06.2016
 * @param <T>
 */
public interface Documents2<T>
{
  public Long persist(T obj);
  
  public void merge(Long key, T obj);
  
  public T find(Long key);
  
  public Set<T> findAll();
  
  public void delete(Long key);
  
  public boolean exists(Long key);
  
  public Set<T> query(Filters filters);
  
  
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
