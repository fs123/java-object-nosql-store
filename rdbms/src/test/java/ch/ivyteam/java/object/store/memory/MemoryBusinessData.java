package ch.ivyteam.java.object.store.memory;

import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessData;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessDataRepositoryExtended.Updater;
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
  public UpdateResult save()
  {
    overwrite();
    return new MemoryUpdateResult();
  }

  @Override
  public UpdateResult overwrite()
  {
    if (meta.getId() == null)
    {
      meta.setId(store.persist(object));
    }
    else
    {
      store.merge(meta.getId(), object);
    }
    return new MemoryUpdateResult();
  }

  @Override
  public UpdateResult delete()
  {
    store.delete(meta.getId());
    return new MemoryUpdateResult();
  }

  @Override
  public boolean isUpToDate()
  {
    return true;
  }

  @Override
  public UpdateResult reload()
  {
    if (meta.getId() != null)
    {
      object = store.find(meta.getId());
    }
    return new MemoryUpdateResult();
  }

  @Override
  public UpdateResult update(Updater<T> updater)
  {
  	boolean done = false;
  	while(!done)
  	{
  		T o = store.find(meta.getId());
  		updater.update(o);
  		store.merge(meta.getId(), o);
  		done = true; // merge could fail, because object was already modified
  	}
    return new MemoryUpdateResult();
  }

  
  private static class MemoryUpdateResult implements UpdateResult
  {
		@Override
		public boolean successfully() {
			return true;
		}
  }
}