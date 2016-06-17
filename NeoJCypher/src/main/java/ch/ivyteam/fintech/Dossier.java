package ch.ivyteam.fintech;

import java.util.HashSet;
import java.util.Set;

public class Dossier
{
  public AccountHolder accountHolder;
  public Set<BeneficialOwner> beneficialOwners = new HashSet<>();
  public ControllingPersonManagement controllingPersonMgmt;
  public LegitimateManagement legitimateMgmt;
}
