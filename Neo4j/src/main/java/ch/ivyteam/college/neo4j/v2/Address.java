package ch.ivyteam.college.neo4j.v2;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity(label="Address")
public class Address extends Entity
{
  public String street;
  public String zip;
  public String city;
}
