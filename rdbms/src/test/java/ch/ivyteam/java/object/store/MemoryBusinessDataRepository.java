package ch.ivyteam.java.object.store;

public class MemoryBusinessDataRepository implements BusinessDataRepository
{

  public MemoryBusinessDataRepository()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public <T> BusinessData<T> create(T obj)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <T> BusinessData<T> find(Long id)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean exists(Long id)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public <T> FluentQuery<T> query(Class<T> resultType)
  {
    // TODO Auto-generated method stub
    return null;
  }


}
