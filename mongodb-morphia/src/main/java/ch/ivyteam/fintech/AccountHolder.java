package ch.ivyteam.fintech;

import java.util.Date;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class AccountHolder extends AddToStringHashCodeAndEquals
{
  public String name;
  public String type; // enum (cooperative, association, foundation, trust, ...)
  public String uidNumber;
  public Boolean registered;
  public Date dateOfRegistry;
  public String nogaCoda;
  public String sectorOrMainActivity;
  public String purposeOfEntity;
  // CAN't HANDLE NUMBERS!
  public int yearOfIncorporation;
  public int numberOfEmployees;
  public Date dateOfSupply;
  public String domicileCountry;
  public String domicileCountryControllingPerson;
  public String language;
  public String contactPerson;
  public String bankBranch;
  
  public Address address;
  public Correspondence correspondence;
}
