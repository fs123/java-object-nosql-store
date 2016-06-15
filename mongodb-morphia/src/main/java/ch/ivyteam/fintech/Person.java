package ch.ivyteam.fintech;

import java.util.Date;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

public class Person extends AddToStringHashCodeAndEquals
{
  public String title;
  public String firstName;
  public String lastName;
  public String nationality;
  public String domicileCountry;
  public Date birthDate;
  public String function;
  public String realtionshipToAccountHolder;
}
