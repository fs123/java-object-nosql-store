package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;

import org.apache.commons.lang3.NotImplementedException;

import ch.ivyteam.java.object.store.rdbms.DocumentSchema.DbType;

public class TstConnectionFactory
{

  public static Connection mysql()
  {
    return JdbcConnectionFactory.createMySql("localhost", "axivy_603_off", "root", "1234");
  }
  
  public static Connection postgre()
  {
    return JdbcConnectionFactory.createPostgres("zugtstdbspos:5432", "tmp_rewJson", "admin", "1234");
  }
  
  public static Connection forDbms(DbType type)
  {
    if (type == DbType.MYSQL)
    {
      return mysql();
    }
    else if (type == DbType.POSTGRES)
    {
      return postgre();
    }
    throw new NotImplementedException("Connection for "+type+" is not yet supported");
  }
  
}
