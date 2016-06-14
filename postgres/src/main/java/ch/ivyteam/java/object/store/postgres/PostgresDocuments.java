package ch.ivyteam.java.object.store.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ch.ivyteam.java.object.store.Documents;

public class PostgresDocuments<T> implements Documents<T> 
{
	private String config;
	private Class<T> type;
	private Connection connection;
	private ObjectMapper mapper = new ObjectMapper();

	public PostgresDocuments(Class<T> type, String config) 
	{
		this.type = type;
		this.config = config;
		try 
		{
			this.connection = DriverManager.getConnection(config);
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	@Override
	public void persist(String key, T obj) 
	{
		String serialized = serialize(obj);
		if (exists(key))
		{
			update(key, serialized);
		}
		else 
		{
			insert(key, serialized);
		}
	}

	private void update(String key, String serialized) 
	{
		try (PreparedStatement stmt = connection.prepareStatement("UPDATE "+getTableName()+" SET "+getTableName()+"Json = ? WHERE "+getTableName()+"Id = ?"))
		{
			stmt.setString(1, serialized);
			stmt.setString(2, key);
			stmt.executeUpdate();
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
	}	

	private void insert(String key, String serialized) 
	{
		try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO "+getTableName()+" ("+getTableName()+"Id, "+getTableName()+"Json) VALUES (?, ?)"))
		{
			stmt.setString(1, key);
			stmt.setString(2, serialized);
			stmt.executeUpdate();
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
	}

	private String serialize(T obj) 
	{
		try 
		{
			return mapper.writeValueAsString(obj);
		} 
		catch (JsonProcessingException ex) 
		{
			throw new RuntimeException(ex);
		}
	}

	private String getTableName()
	{
		return type.getSimpleName();
	}

	@Override
	public T find(String key) 
	{
		try (PreparedStatement stmt = connection.prepareStatement("SELECT "+getTableName()+"Json FROM "+getTableName()+" WHERE "+getTableName()+"Id = ?"))
		{
			stmt.setString(1, key);
			try (ResultSet result = stmt.executeQuery())
			{
				if (result.next())
				{
					return deserialize(result.getString(1));
				}
				throw new RuntimeException("Object with key "+ key+" not found");
			}
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
	}

	private T deserialize(String json) 
	{
		try 
		{
			return mapper.readValue(json, type);
		} 
		catch (Exception e) 
		{
			throw new RuntimeException(e);
		} 
	}

	@Override
	public Collection<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean exists(String key) 
	{
		try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) FROM "+getTableName()+" WHERE "+getTableName()+"Id = ?"))
		{
			stmt.setString(1, key);
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
	public Collection<T> query(ch.ivyteam.java.object.store.Documents.Filters filters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<T> query(String filter) 
	{
		try(PreparedStatement stmt = connection.prepareStatement("SELECT "+getTableName()+"Json FROM "+getTableName()+" WHERE "+filter))
		{
			try(ResultSet result = stmt.executeQuery())
			{
				List<T> dossiers = new ArrayList<>();
				while (result.next())
				{
					dossiers.add(deserialize(result.getString(1)));
				}
				return dossiers;
			}
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
	}

}
