package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Contact extends Entity
{
  public String landlinePhone;
  public String mobilePhone;
  public String businessPhone;
  public String eMail;
}