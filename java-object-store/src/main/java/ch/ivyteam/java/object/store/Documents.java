package ch.ivyteam.java.object.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.ivyteam.fintech.Dossier;

public interface Documents<T>
{
  public void persist(String key, T obj);
  
  public T find(String key);
  
  public Collection<T> findAll();
  
  public void remove(String key);
  
  public boolean exists(String key);
  
  public Collection<T> query(Filters filters);
  
  public Collection<T> query(String string);
  
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
