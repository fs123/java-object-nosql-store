package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Legitimate extends Entity
{
  public String signatoryRightsRegister; // enum (Sole, Joint, ...)
  public String signatoryRightsBank; // enum SINGLE|ROLE
  public Person person;
  public Contact contact;
  public Address address;
}
