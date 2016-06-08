package ch.ivyteam.java.object.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface Documents<T>
{
  public void persist(String key, T obj);
  
  public T find(String key);
  
  public void remove(String key);
  
  public boolean exists(String key);
  
  public List<T> query(Filters filters);
  
  
  public static class Filters
  {
    private final Map<String, String> filters = new HashMap<>();
    
    public void fieldFilter(String fieldName, String value)
    {
      filters.put(fieldName, value);
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
