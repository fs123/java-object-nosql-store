package ch.ivyteam.java.object.jcypher;

import java.util.Properties;

import iot.jcypher.database.DBAccessFactory;
import iot.jcypher.database.DBProperties;
import iot.jcypher.database.DBType;
import iot.jcypher.database.IDBAccess;

public class DBAccess
{

  public static IDBAccess localInstance()
  {
    Properties remoteProperties = new Properties();
    remoteProperties.put(DBProperties.SERVER_ROOT_URI, "http://@localhost:7474");
    return DBAccessFactory.createDBAccess(DBType.REMOTE, remoteProperties, "neo4j", "neo4j");
  }
  
  public static IDBAccess inMemory()
  {
    return DBAccessFactory.createDBAccess(DBType.IN_MEMORY, new Properties());
  }
  
}
