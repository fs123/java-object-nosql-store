package ch.ivyteam.fintech;

public class Legitimate
{
  public String signatoryRightsRegister; // enum (Sole, Joint, ...)
  public String signatoryRightsBank; // enum SINGLE|ROLE
  public Person person;
  public Contact contact;
  public Address address;
}
