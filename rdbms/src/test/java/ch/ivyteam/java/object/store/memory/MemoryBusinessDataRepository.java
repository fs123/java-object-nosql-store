package ch.ivyteam.java.object.store.memory;

import ch.ivyteam.java.object.store.BusinessDataRepository;
import ch.ivyteam.java.object.store.BusinessDataRepository.BusinessDataRepositoryExtended.Updater;

public class MemoryBusinessDataRepository implements BusinessDataRepository
{
  MemoryObjectStore<?> internalStore = new MemoryObjectStore<>();
  
  public MemoryBusinessDataRepository()
  {
  }

  @Override
  public <T> BusinessData<T> create(T obj)
  {
    return MemoryBusinessData.of(obj, getStore());
  }

  @SuppressWarnings("unchecked")
  private <T> MemoryObjectStore<T> getStore()
  {
    return (MemoryObjectStore<T>)internalStore;
  }

  @Override
  public <T> BusinessData<T> find(Long id)
  {
    MemoryObjectStore<T> store = getStore();
    T object = store.find(id);
    if (object == null)
    {
      return null;
    }
    MemoryBusinessData<T> bd = MemoryBusinessData.of(object, store);
    bd.getMeta().setId(id);
    return bd;
  }

  @Override
  public boolean exists(Long id)
  {
    return getStore().exists(id);
  }

  @Override
  public <T> FluentQuery<T> query(Class<T> resultType)
  {
    return new MemoryQuery<T>(getStore());
  }

	@SuppressWarnings("unchecked")
	@Override
	public <T> boolean update(Long id, Updater<T> updater)
	{
		return find(id).update((Updater<Object>) updater).successfully();
	}
}
