package ch.ivyteam.java.object.store;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import ch.ivyteam.java.object.store.rdbms.DocumentSchema;
import ch.ivyteam.java.object.store.rdbms.DocumentSchema.DbType;
import ch.ivyteam.java.object.store.rdbms.SysDbRepository;
import ch.ivyteam.java.object.store.rdbms.TstConnectionFactory;

@RunWith(Parameterized.class)
public abstract class AbstractRdbmsTest
{
  @Parameter
  public DbType dbType;
  
  @Parameters(name = "{index}: rdbms - {0}")
  public static Collection<Object[]> data() {
    List<Object[]> d = new ArrayList<>();
    for(DbType type : Arrays.asList(DbType.values()))
    {
      d.add(new Object[]{type});
    }
    return d;
  }
  
  private Connection connection;
  
  @Before
  public void setup()
  {
    connection =  TstConnectionFactory.forDbms(dbType);
    new DocumentSchema(connection).create(dbType);
  }
  
  @After
  public void tearDown()
  {
    new DocumentSchema(connection).drop(dbType);
    closeConnection();
  }

  protected void closeConnection()
  {
    try
    {
      connection.close();
    }
    catch (SQLException ex)
    {
      throw new RuntimeException(ex);
    }
  }
  
  public <T> ObjectStore<T> storeOf(Class<T> type)
  {
    return new SysDbRepository<>(type, connection);
  }
  
}
