package ch.ivyteam.java.object.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public interface Documents<T, F>
{
  public String persist(String key, T obj);
  
  public T findById(String key);
  
  public void remove(String key);
  
  public List<T> query(Filters<F> filters);
  
  
  public static class Filters<F>
  {
    private final Map<F, String> filters = new HashMap<>();
    
    public void fieldFilter(F fieldName, String value)
    {
      filters.put(fieldName, value);
    }
    
    public Set<Entry<F, String>> getFieldFilters()
    {
      return filters.entrySet();
    }
    
    public boolean isEmpty()
    {
      return filters.isEmpty();
    }
  }
  
}
