package ch.ivyteam.fintech.neo4j;

import java.util.Date;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Person extends Entity
{
  public String title;
  public String firstName;
  public String lastName;
  public String nationality;
  public String domicileCountry;
  @DateLong 
  public Date birthDate;
  public String function;
  public String realtionshipToAccountHolder;
}
