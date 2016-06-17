package ch.ivyteam.java.object.store.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.ivyteam.java.object.store.Documents;
import ch.ivyteam.java.object.store.serialization.Serializer;
import ch.ivyteam.java.object.store.serialization.JacksonSerializer;

public class PostgresDocuments<T> implements Documents<T> 
{
	private String config;
	private Class<T> type;
	private Connection connection;
	private Serializer<T> serializer; 

	public PostgresDocuments(Class<T> type, String config) 
	{
		this(type, config, new JacksonSerializer<T>(type));
	}

	public PostgresDocuments(Class<T> type, String config, Serializer<T> serializer2) {
		this.type = type;
		this.config = config;
		serializer = serializer2;
		try 
		{
			this.connection = DriverManager.getConnection(config);
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void persist(String key, T obj) 
	{
		String serialized = serializer.serialize(obj);
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
					return serializer.deserialize(result.getString(1));
				}
				throw new RuntimeException("Object with key "+ key+" not found");
			}
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
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
					dossiers.add(serializer.deserialize(result.getString(1)));
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
