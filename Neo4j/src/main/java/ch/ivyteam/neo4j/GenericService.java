package ch.ivyteam.neo4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.neo4j.ogm.session.Session;

import ch.ivyteam.neo4j.domain.Entity;

public abstract class GenericService<T>{

  private interface Depth
  {
    int LIST = 0;
    int ENTITY = 1;
  }
  
  protected final Session session;
  private final Class<T> type;

  public GenericService(Session session, Class<T> type)
  {
    this.type = type;
    this.session = session;
  }

  public T find(Long id)
  {
    return session.load(getEntityType(), id, Depth.ENTITY);
  }
  
  public T persist(T entity) {
    session.save(entity, Depth.ENTITY);
    return find(((Entity) entity).getId());
  }
  
  public List<T> getAll() {
      return listify(session.loadAll(getEntityType(), Depth.LIST));
  }

  public void delete(Long id) {
      session.delete(session.load(getEntityType(), id));
  }
  
  public List<T> query(String cypher)
  {
    return listify(session.query(getEntityType(), cypher, Collections.emptyMap()));
  }

  private static <T> List<T> listify(Iterable<T> entities)
  {
    List<T> listified = new ArrayList<>();
    for(T entity : entities)
    {
      listified.add(entity);
    }
    return listified;
  }
  
  public Class<T> getEntityType()
  {
    return type;
  }
 }