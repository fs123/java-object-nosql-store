package ch.ivyteam.java.object.store.memory;

import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData.MetaData;

public class MemoryMetaData<T> implements MetaData<T>
{
  private final Class<T> type;
  private Long id;

  public MemoryMetaData(Class<T> type, Long id)
  {
    this.type = type;
    this.id = id;
  }

  @Override
  public Long getId()
  {
    return id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }

  @Override
  public long getVersion()
  {
    return 0;
  }

  @Override
  public String getTypeRaw()
  {
    return type.getSimpleName();
  }

  @Override
  public Class<T> getType()
  {
    return type;
  }

}
