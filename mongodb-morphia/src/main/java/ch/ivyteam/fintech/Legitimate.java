package ch.ivyteam.fintech;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class Legitimate extends AddToStringHashCodeAndEquals
{
  public String signatoryRightsRegister; // enum (Sole, Joint, ...)
  public String signatoryRightsBank; // enum SINGLE|ROLE
  public Person person;
  public Contact contact;
  public Address address;
}
