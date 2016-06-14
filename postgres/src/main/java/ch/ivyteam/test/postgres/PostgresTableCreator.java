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
		createTestTable();
	}

	private void createTestTable() {
		String sql = "CREATE TABLE Dossier (DossierId VARCHAR(200) primary key, DossierJson JSONB)";
		
		try (PreparedStatement stmt = connection.prepareStatement(sql))
		{
			stmt.execute();
		} 
		catch (SQLException ex) 
		{
			throw new RuntimeException(ex);
		}
		System.out.println("Table 'Dossier' created");
	}

}
