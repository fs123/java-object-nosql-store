package ch.ivyteam.fintech.neo4j;

import java.util.Date;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class AccountHolder extends Entity
{
  public String name;
  public String type; // enum (cooperative, association, foundation, trust, ...)
  public String uidNumber;
  public Boolean registered;
  @DateLong 
  public Date dateOfRegistry;
  public String nogaCoda;
  public String sectorOrMainActivity;
  public String purposeOfEntity;
  public Number yearOfIncorporation;
  public int numberOfEmployees;
  @DateLong
  public Date dateOfSupply;
  public String domicileCountry;
  public String domicileCountryControllingPerson;
  public String language;
  public String contactPerson;
  public String bankBranch;
  
  @Relationship(type="LOCATED_AT", direction=Relationship.OUTGOING)
  public Address address;
  @Relationship(type="CORRESPONDS", direction=Relationship.OUTGOING)
  public Correspondence correspondence;
}
