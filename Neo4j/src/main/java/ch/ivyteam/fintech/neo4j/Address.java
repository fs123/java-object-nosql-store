package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Address extends Entity
{
  public String street;
  public String poBox;
  public String zipCode;
  public String city;
  public String country;
}
