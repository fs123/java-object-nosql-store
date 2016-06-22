package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.NotImplementedException;

public class DocumentSchema
{
  public static final String TABLE_NAME = "IWA_BusinessData4";
  
  private final Connection connection;
  
  public DocumentSchema(Connection connection)
  {
    this.connection = connection;
  }

  public static enum DbType
  {
    MYSQL,
    POSTGRES,
    MSSQL;
  }

  public void create(DbType type)
  {
    try
    {
      if (type == DbType.MYSQL)
      {
        createMySql();
      }
      else
      {
        throw new NotImplementedException("Creation of DB schema is not implemented for "+type);
      }
    } 
    catch (SQLException ex)
    {
      throw new RuntimeException("Failed to create DB table "+TABLE_NAME, ex);
    }
  }

  private void createMySql() throws SQLException
  {
    try
    {
      connection.createStatement().execute("select 1 from `"+TABLE_NAME+"` LIMIT 1");
    }
    catch (SQLException ex)
    { // schema does not exist
      connection.createStatement().execute("CREATE TABLE `"+TABLE_NAME+"` ("
                + " `id` INT(10) NOT NULL AUTO_INCREMENT,"
                + "`type` VARCHAR(500) NOT NULL,"
                + "`json` TEXT NULL,"
                + "PRIMARY KEY (`id`)"
              + ")"
              + "COLLATE='utf8_general_ci'"
              + "ENGINE=InnoDB;");
    }
  }
  
  public void drop(DbType type)
  {
    try
    {
      if (type == DbType.MYSQL)
      {
        connection.createStatement().executeUpdate("DROP TABLE `"+TABLE_NAME+"`");
      }
      else
      {
        throw new NotImplementedException("Creation of DB schema is not implemented for "+type);
      }
    }
    catch (SQLException ex)
    {
      throw new RuntimeException("Can not drop TABLE", ex);
    }
  }
  
}
