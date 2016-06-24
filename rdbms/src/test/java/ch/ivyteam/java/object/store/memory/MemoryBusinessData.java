package ch.ivyteam.java.object.store.memory;

import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData;
import ch.ivyteam.java.object.store.ObjectStore;

public class MemoryBusinessData<T> implements BusinessData<T>
{
  private ObjectStore<T> store;

  private final MemoryMetaData<T> meta;
  private T object;
  
  public MemoryBusinessData(ObjectStore<T> store, Class<T> type, Long id)
  {
    this.store = store;
    this.meta = new MemoryMetaData<>(type, id);
  }
  
  public static <T> MemoryBusinessData<T> of(T object, ObjectStore<T> store)
  {
    @SuppressWarnings("unchecked")
    MemoryBusinessData<T> memoryData = new MemoryBusinessData<T>(store, 
            (Class<T>)object.getClass(), null);
    memoryData.object = object;
    return memoryData;
  }
  
  @Override
  public MemoryMetaData<T> getMeta()
  {
    return meta;
  }

  @Override
  public T object()
  {
    return object;
  }

  @Override
  public boolean save()
  {
    overwrite();
    return true;
  }

  @Override
  public void overwrite()
  {
    if (meta.getId() == null)
    {
      meta.setId(store.persist(object));
    }
    else
    {
      store.merge(meta.getId(), object);
    }
  }

  @Override
  public boolean delete()
  {
    store.delete(meta.getId());
    return true;
  }

  @Override
  public boolean isUpToDate()
  {
    return true;
  }

  @Override
  public boolean reload()
  {
    if (meta.getId() != null)
    {
      object = store.find(meta.getId());
    }
    return true;
  }

  @Override
  public void update(ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData.Updater<T> updater)
  {
    updater.update(object);
    save();
  }

  @Override
  public void lockAndUpdate(
          ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData.Updater<T> updater)
  {
    updater.update(object);
    save();
  }
}