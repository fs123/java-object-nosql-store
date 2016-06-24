package ch.ivyteam.java.object.store;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Draft of an object repository for Business Data
 * 
 * @since 22.06.2016
 */
public interface BusinessDataRepository
{
  public <T> BusinessData<T> create(T obj);
  public <T> BusinessData<T> find(Long id);
  public boolean exists(Long id);
  
  /**
   * <h1 style="color:red">Preliminary!</h1>
   * @param resultType
   * @return fluid query
   */
  public <T> FluentQuery<T> query(Class<T> resultType);
  
  public static interface FluentQuery<T>
  {
    public FieldOperator<T> field(String fieldName);
    
    public static interface FieldOperator<T>
    {
      public FluentQuery<T> contains(String value);
      public FluentQuery<T> isEqualTo(String value);
      public FluentQuery<T> isEqualTo(Number value);
    }
    
    public QueryResult<T> execute();

    public static interface QueryResult<T> 
    {
      List<T> objects();
      List<BusinessData<T>> data();
      
      public static interface DocumentResImpl<T>
      {
        Stream<BusinessData<T>> docStream();
        Stream<T> dataStream();
      }
    }
  }
  
  public static interface Bd2RepImpl<T>
  {
    public Set<Long> create(List<T> obj); 
    public Set<T> findAll();
    public void delete(Long id);
  }
  
  public static interface BusinessData<T> {
    MetaData<T> getMeta();
    
    T object();
    
    public static interface DocumentImpl
    {
      String rawData();
    }
    
    public static interface MetaData<T> {
      long getId();
      long getVersion();
      String getTypeRaw();
      Class<T> getType();
    }

    boolean save();
    void overwrite();
    boolean delete();
    
    boolean isUpToDate();
    boolean reload();
    
    public void update(Updater<T> updater);
    public void lockAndUpdate(Updater<T> updater);
    
    @FunctionalInterface
    public static interface Updater<T>
    {
      public void update(T object); 
    }
  }
  
}
