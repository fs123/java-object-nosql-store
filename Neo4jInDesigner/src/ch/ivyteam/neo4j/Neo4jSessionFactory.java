package ch.ivyteam.neo4j;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

  private static final String COMMON = "ch.ivyteam.neo4j.domain";
  private static final SessionFactory sessionFactory = new SessionFactory("ch.ivyteam.college.neo4j", COMMON);
  private static final SessionFactory sessionFactoryV2 = new SessionFactory("ch.ivyteam.college.neo4j.v2", COMMON);
  private static final SessionFactory finFactory = new SessionFactory("ch.ivyteam.fintech.neo4j", COMMON); 
  private static final SessionFactory serializationFactory = new SessionFactory("ch.ivyteam.serialize.neo4j", COMMON); 
  
  private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

  public static Neo4jSessionFactory getInstance() {
    return factory;
  }

  private Neo4jSessionFactory() {
  }

  public Session getNeo4jSessionV1() {
      return sessionFactory.openSession();
  }
  
  public Session getNeo4jSessionV2() {
    return sessionFactoryV2.openSession();
  }
  
  public Session getFinSession()
  {
    return finFactory.openSession();
  }

  public Session getSerializationSession()
  {
    return serializationFactory.openSession();
  }
  
}