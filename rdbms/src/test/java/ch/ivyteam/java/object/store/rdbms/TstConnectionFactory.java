package ch.ivyteam.java.object.store.rdbms;

import java.sql.Connection;

public class TstConnectionFactory
{

  public static Connection mysql()
  {
    return JdbcConnectionFactory.createMySql("axivy_603_off", "root", "1234");
  }
  
}
