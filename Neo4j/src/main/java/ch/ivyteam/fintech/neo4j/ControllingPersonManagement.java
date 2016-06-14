package ch.ivyteam.fintech.neo4j;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import ch.ivyteam.neo4j.domain.Entity;

@NodeEntity
public class ControllingPersonManagement extends Entity
{
  public boolean fiduciaryHoldingAssets;
  @Relationship(type="CONTROLLED_BY", direction=Relationship.OUTGOING)
  public Set<ControllingPerson> controllingPersons = new HashSet<>();
}
