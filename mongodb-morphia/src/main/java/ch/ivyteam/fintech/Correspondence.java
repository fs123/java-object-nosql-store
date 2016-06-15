package ch.ivyteam.fintech;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class Correspondence extends AddToStringHashCodeAndEquals
{
  public String salutation;
  public String title;
  public String firstName;
  public String lastName;
  public String function;
  public Contact contact;
  public String language; // german, french, italian, english
  public boolean differentAddress;
  public Address address;
}
