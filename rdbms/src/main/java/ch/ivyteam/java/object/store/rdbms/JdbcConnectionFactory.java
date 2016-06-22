package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionFactory
{

  public static Connection createMySql(String host, String db, String user, String password)
  {
    loadClass("com.mysql.jdbc.Driver");
    return connect("jdbc:mysql://"+host+"/"+db+"?user="+user+"&password="+password);
  }
  
  public static Connection createPostgres(String hostAndPort, String db, String user, String password)
  {
    loadClass("org.postgresql.Driver");
    return connect("jdbc:postgresql://"+hostAndPort+"/"+db+"?user="+user+"&password="+password+"&stringtype=unspecified");
  }
  
  private static void loadClass(String driver)
  {
    try
    {
      Class.forName(driver);
    }
    catch (ClassNotFoundException ex)
    {
      throw new RuntimeException("can not access driver "+driver, ex);
    }
  }
  
  public static Connection connect(String jdbcUrl)
  {
    try
    {
      return DriverManager.getConnection(jdbcUrl);
    }
    catch (SQLException ex)
    {
      throw new RuntimeException("Unable to establish JDBC connection", ex);
    }
  }
  
}
