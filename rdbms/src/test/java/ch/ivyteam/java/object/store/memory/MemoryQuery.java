package ch.ivyteam.java.object.store.memory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import ch.ivyteam.java.object.store.BusinessDataRepository;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData;
import ch.ivyteam.java.object.store.BusinessDataRepository.FluentQuery;
import ch.ivyteam.java.object.store.ObjectStore;
import ch.ivyteam.java.object.store.ObjectStore.Filters;

public class MemoryQuery<T> implements BusinessDataRepository.FluentQuery<T>
{
  private Filters filters;
  private ObjectStore<T> internalStore;

  public MemoryQuery(ObjectStore<T> internalStore)
  {
    this.internalStore = internalStore;
    this.filters = new ObjectStore.Filters();
  }

  @Override
  public BusinessDataRepository.FluentQuery.FieldOperator<T> field(
          String fieldName)
  {
    return new MemoryOperator<T>(fieldName);
  }

  @Override
  public BusinessDataRepository.FluentQuery.QueryResult<T> execute()
  {
    Map<Long, T> objects = internalStore.query(filters);
    
    Set<BusinessData<T>> data = new HashSet<>();
    for(Entry<Long, T> entry : objects.entrySet())
    {
      MemoryBusinessData<T> memoryData = MemoryBusinessData.of(entry.getValue(), internalStore);
      memoryData.getMeta().setId(entry.getKey());
      data.add(memoryData);
    }
    
    return new QueryResult<T>(data);
  }
  
  @SuppressWarnings({"unchecked", "hiding"})
  private class MemoryOperator<T> implements BusinessDataRepository.FluentQuery.FieldOperator<T>
  {
    private final String fieldName;

    public MemoryOperator(String fieldName)
    {
      this.fieldName = fieldName;
    }
    
    @Override
    public BusinessDataRepository.FluentQuery<T> contains(String value)
    {
      return (FluentQuery<T>) MemoryQuery.this;
    }

    @Override
    public BusinessDataRepository.FluentQuery<T> isEqualTo(String value)
    {
      filters.field(fieldName, value);
      return (FluentQuery<T>) MemoryQuery.this;
    }

    @Override
    public BusinessDataRepository.FluentQuery<T> isEqualTo(Number value)
    {
      return (FluentQuery<T>) MemoryQuery.this;
    }
  }
  
  @SuppressWarnings("hiding")
  private class QueryResult<T> implements BusinessDataRepository.FluentQuery.QueryResult<T>
  {
    private final Set<BusinessData<T>> data;

    public QueryResult(Set<BusinessData<T>> data)
    {
      this.data = data;
    }

    @Override
    public List<T> objects()
    {
      return data.stream()
              .map(d -> d.object())
              .collect(Collectors.toList());
    }

    @Override
    public List<BusinessData<T>> data()
    {
      return new ArrayList<>(data);
    }
  }

}
