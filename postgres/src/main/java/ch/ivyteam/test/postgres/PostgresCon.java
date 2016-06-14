package ch.ivyteam.test.postgres;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Requires PostgreSQL 9.5 and later
 * JSON Types: https://www.postgresql.org/docs/9.5/static/datatype-json.html
 * JSON Functions: https://www.postgresql.org/docs/9.5/static/functions-json.html
 */
public class PostgresCon {
	public final static String connectionString = "jdbc:postgresql://localhost:5433/testjson?user=postgres&stringtype=unspecified"; // Default Postgres Port is 5432

	public static void main(String[] args) {
		try {
			Connection connection = getConnection();
			PostgresTableCreator postgresTableCreator = new PostgresTableCreator(connection);
			postgresTableCreator.createTables();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Connection getConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection(connectionString);
		return connection;
	}
	

}
