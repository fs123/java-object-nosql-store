package ch.ivyteam.test.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresTableCreator {
	private Connection connection;

	public PostgresTableCreator(Connection connection) {
		super();
		this.connection = connection;
	}
	
	public void createTables() {
		createTestTable("Dossier");
		createTestTable("Recursion");
		createTestTable("Zoo");
		createTestTable("SameReference");
	}
	
	private void createTestTable(String tableName) {
		String sql = "CREATE TABLE " + tableName  + " (" + tableName  + "Id VARCHAR(200) primary key, " + tableName  + "Json JSONB)";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql))
		{
			stmt.execute();
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
		System.out.println("Table '" + tableName  + "' created");
	}


}
