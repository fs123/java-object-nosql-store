package ch.ivyteam.shop.model.v1;

public class ProductOptions extends AddToStringHashCodeAndEquals
{
  // Good to know: 
  // If the values in the mondodb are null (or does not exists) 
  // the blow default value will be present
  public boolean hasSecurityBug = false;
  public boolean hasTurboBooster = true;
  public String specialOfferText = "Today 30% off!";
  public Double length = 123.45;
}
