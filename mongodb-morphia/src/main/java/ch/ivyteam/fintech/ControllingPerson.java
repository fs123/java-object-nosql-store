package ch.ivyteam.fintech;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class ControllingPerson extends AddToStringHashCodeAndEquals
{
  public int share;
  public String shareConfirmation; // enum...
  public Person person;
  public Address address;
}
