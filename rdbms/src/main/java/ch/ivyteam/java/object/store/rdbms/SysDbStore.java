package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.serialize.JsonIOSerializer;
import ch.ivyteam.java.object.store.serialize.Serializer;

public class SysDbStore<T> implements Documents<T>
{
  private final Class<T> type;
  private final Connection connection;
  private final Serializer<T> serializer;

  public SysDbStore(Class<T> type, Connection connection)
  {
    this.type = type;
    this.connection = connection;
    this.serializer = new JsonIOSerializer<>(type);
  }

  @Override
  public void persist(String key, T obj)
  {
    String json = serializer.serialize(obj);
    if (key == null)
    {
      insert(json);
    }
    else if (exists(key))
    {
      update(key, json);
    }
    else
    {
      insert(key, json);
    }
  }

  private void update(String key, String serialized)
  {
    try (PreparedStatement stmt = connection.prepareStatement("UPDATE `" + DocumentSchema.TABLE_NAME + "` "
            + "SET json = ? "
            + "WHERE id = ? and `type`= ?"))
    {
      stmt.setString(1, serialized);
      stmt.setString(2, key);
      stmt.setString(3, type.getName());
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private void insert(String key, String serialized)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("INSERT INTO " + DocumentSchema.TABLE_NAME + " "
                            + "(id, type, json) VALUES (?, ?, ?)"))
    {
      stmt.setString(1, key);
      stmt.setString(2, type.getName());
      stmt.setString(3, serialized);
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private void insert(String serialized)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("INSERT INTO " + DocumentSchema.TABLE_NAME + " "
                            + "(type, json) VALUES (?, ?)"))
    {
      stmt.setString(1, type.getName());
      stmt.setString(2, serialized);
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public T find(String key)
  {
    try (PreparedStatement stmt = connection.prepareStatement(
            "SELECT json FROM " + DocumentSchema.TABLE_NAME + " "
            + "WHERE id = ? AND type= ?"))
    {
      stmt.setString(1, key);
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
  public Collection<T> findAll()
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
  public void remove(String key)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("DELETE FROM " + DocumentSchema.TABLE_NAME + " "
                    + "WHERE id = ? AND type = ?"))
    {
      stmt.setString(1, key);
      stmt.setString(2, type.getName());
      stmt.executeUpdate();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean exists(String key)
  {
    try (PreparedStatement stmt = connection
            .prepareStatement("SELECT COUNT(*) FROM " + DocumentSchema.TABLE_NAME + " "
                    + "WHERE id = ? AND type = ?"))
    {
      stmt.setString(1, key);
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
  public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filters)
  {
    if (filters.isEmpty())
    {
      return Collections.emptyList();
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

  private Collection<T> jsonQueryToDocs(PreparedStatement stmt) throws SQLException
  {
    try (ResultSet result = stmt.executeQuery())
    {
      List<T> docs = new ArrayList<>();
      while (result.next() == true)
      {
        docs.add(serializer.deserialize(result.getString(1)));
      }
      return docs;
    }
  }

}
