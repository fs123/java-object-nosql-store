package ch.ivyteam.fintech.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class BeneficialOwner extends Entity
{
  @Relationship(type="BENEFIT_BY", direction=Relationship.OUTGOING)
  public Person person;
  @Relationship(type="BENEFIT_LOCATION", direction=Relationship.OUTGOING)
  public Address address;
  @Relationship(type="BENEFITS", direction=Relationship.INCOMING)
  public Dossier dossier;
}
