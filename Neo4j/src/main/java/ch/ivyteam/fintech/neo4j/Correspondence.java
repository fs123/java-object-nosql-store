package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Correspondence extends Entity
{
  public String salutation;
  public String title;
  public String firstName;
  public String lastName;
  public String function;
  @Relationship(type="CONTACT", direction=Relationship.OUTGOING)
  public Contact contact;
  public String language; // german, french, italian, english
  public boolean differentAddress;
  @Relationship(type="LOCATION", direction=Relationship.OUTGOING)
  public Address address;
}
