package ch.ivyteam.fintech;

import java.util.HashSet;
import java.util.Set;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class LegitimateManagement extends AddToStringHashCodeAndEquals
{
  public boolean onlyLegitimatesAreBO;
  public Set<Legitimate> legitimates = new HashSet<>();
}
