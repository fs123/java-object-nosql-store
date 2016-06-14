package ch.ivyteam.fintech.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class Dossier extends Entity
{
  @Relationship(type="HELD_BY", direction=Relationship.OUTGOING)
  public AccountHolder accountHolder;
  @Relationship(type="BENEFITS", direction=Relationship.OUTGOING)
  public Set<BeneficialOwner> beneficialOwners = new HashSet<>();
  @Relationship(type="CONTROLLED_TROUGH", direction=Relationship.OUTGOING)
  public ControllingPersonManagement controllingPersonMgmt;
  @Relationship(type="LEGITIMATED_TROUGH", direction=Relationship.OUTGOING)
  public LegitimateManagement legitimateMgmt;
}
