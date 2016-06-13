package ch.ivyteam.neo4j;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

  private static final String COMMON = "ch.ivyteam.neo4j.domain";
  private final static SessionFactory sessionFactory = new SessionFactory("ch.ivyteam.college.neo4j", COMMON);
  private final static SessionFactory sessionFactoryV2 = new SessionFactory("ch.ivyteam.college.neo4j.v2", COMMON);
  
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
  
}