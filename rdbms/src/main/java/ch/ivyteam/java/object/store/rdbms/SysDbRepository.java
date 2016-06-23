package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import ch.ivyteam.java.object.store.BusinessDataRepository;
import ch.ivyteam.java.object.store.serialize.JsonIOSerializer;
import ch.ivyteam.java.object.store.serialize.Serializer;

public class SysDbRepository<T> implements BusinessDataRepository<T>
{
  private final Class<T> type;
  private final Connection connection;
  private final Serializer<T> serializer;

  public SysDbRepository(Class<T> type, Connection connection)
  {
    this.type = type;
    this.connection = connection;
    this.serializer = new JsonIOSerializer<>(type);
  }
  
  @Override
  public Long persist(T obj)
  {
    String json = serializer.serialize(obj);
    try (PreparedStatement stmt = connection
            .prepareStatement("INSERT INTO " + DocumentSchema.TABLE_NAME + " "
                            + "(type, json) VALUES (?, ?)"))
    {
      stmt.setString(1, type.getName());
      stmt.setString(2, json);
      return (long) stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Set<Long> persist(List<T> objs)
  {
    if (objs == null || objs.isEmpty())
    {
      return Collections.emptySet();
    }
    
    try (PreparedStatement stmt = connection
            .prepareStatement("INSERT INTO " + DocumentSchema.TABLE_NAME + " "
                            + "(type, json) VALUES (?, ?)"))
    {
      for(T obj : objs)
      {
        stmt.setString(1, type.getName());
        stmt.setString(2, serializer.serialize(obj));
        stmt.addBatch();
      }
      
      int[] result = stmt.executeBatch();
      Set<Long> ids = new HashSet<>(result.length);
      for(int id : result)
      {
        ids.add((long)id);
      }
      return ids;
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void merge(Long key, T obj)
  {
    if (key == null)
    {
      persist(obj);
      return;
    }
    if (exists(key))
    {
      update(key, obj);
      return;
    }
    
    insert(key, obj);
  }

  private void update(Long key, T obj)
  {
    String json = serializer.serialize(obj);
    try (PreparedStatement stmt = connection.prepareStatement("UPDATE `" + DocumentSchema.TABLE_NAME + "` "
            + "SET json = ? "
            + "WHERE id = ? and `type`= ?"))
    {
      stmt.setString(1, json);
      stmt.setLong(2, key);
      stmt.setString(3, type.getName());
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private void insert(Long key, T obj)
  {
    String json = serializer.serialize(obj);
    try (PreparedStatement stmt = connection
            .prepareStatement("INSERT INTO " + DocumentSchema.TABLE_NAME + " "
                            + "(id, type, json) VALUES (?, ?, ?)"))
    {
      stmt.setLong(1, key);
      stmt.setString(2, type.getName());
      stmt.setString(3, json);
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  @Override
  public T find(Long key)
  {
    try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT json FROM " + DocumentSchema.TABLE_NAME + " "
            + "WHERE id = ? AND type= ?"))
    {
      stmt.setLong(1, key);
      stmt.setString(2, type.getName());
      try (ResultSet result = stmt.executeQuery())
      {
        if (result.next())
        {
          return serializer.deserialize(result.getString(1));
        }
        throw new RuntimeException("Object with key " + key + " not found");
      }
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Set<T> findAll()
  {
    try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT json FROM " + DocumentSchema.TABLE_NAME + " WHERE type= ?"))
    {
      stmt.setString(1, type.getName());
      return jsonQueryToDocs(stmt);
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Set<T> query(ch.ivyteam.java.object.store.BusinessDataRepository.Filters filters)
  {
    if (filters.isEmpty())
    {
      return Collections.emptySet();
    }

    StringBuilder sql = new StringBuilder("SELECT json ")
         .append("FROM " + DocumentSchema.TABLE_NAME + " ")
         .append("WHERE type= ?");
    for(int i=0; i<filters.getFieldFilters().size(); i++)
    {
      if (i == 0)
      {
        sql.append("AND (");
      }
      else
      {
        sql.append(" OR ");
      }
      sql.append("json LIKE ?");
    }
    sql.append(")");
    
    try (PreparedStatement stmt = connection.prepareStatement(
            sql.toString()))
    {
      stmt.setString(1, type.getName());
      
      int param=2;
      for(Entry<String, String> fieldFilter : filters.getFieldFilters())
      {
        String jsonPart = "%\""+fieldFilter.getKey()+"\":\""+fieldFilter.getValue()+"\"%";
        stmt.setString(param, jsonPart);
        param++;
      }
      return jsonQueryToDocs(stmt);
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean exists(Long key)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("SELECT COUNT(*) FROM " + DocumentSchema.TABLE_NAME + " "
                    + "WHERE id = ? AND type = ?"))
    {
      stmt.setLong(1, key);
      stmt.setString(2, type.getName());
      try (ResultSet result = stmt.executeQuery())
      {
        result.next();
        return result.getInt(1) > 0;
      }
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void delete(Long key)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("DELETE FROM " + DocumentSchema.TABLE_NAME + " "
                    + "WHERE id = ? AND type = ?"))
    {
      stmt.setLong(1, key);
      stmt.setString(2, type.getName());
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  private Set<T> jsonQueryToDocs(PreparedStatement stmt) throws SQLException
  {
    try (ResultSet result = stmt.executeQuery())
    {
      Set<T> docs = new HashSet<>();
      while (result.next() == true)
      {
        docs.add(serializer.deserialize(result.getString(1)));
      }
      return docs;
    }
  }

}
