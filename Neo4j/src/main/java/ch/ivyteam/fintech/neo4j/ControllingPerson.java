package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class ControllingPerson extends Entity
{
  public int share;
  public String shareConfirmation; // enum...
  public Person person;
  public Address address;
}
