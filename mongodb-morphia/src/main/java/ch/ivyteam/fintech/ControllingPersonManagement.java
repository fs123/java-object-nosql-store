package ch.ivyteam.fintech;

import java.util.HashSet;
import java.util.Set;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class ControllingPersonManagement extends AddToStringHashCodeAndEquals
{
  public boolean fiduciaryHoldingAssets;
  public Set<ControllingPerson> controllingPersons = new HashSet<>();
}
