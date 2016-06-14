//package ch.ivyteam.shop.model.v2;
//
//import org.mongodb.morphia.annotations.AlsoLoad;
//
//import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;
//
//public class ProductOptionsV2 extends AddToStringHashCodeAndEquals
//{
//  // Good to know: 
//  // If the values in the mondodb are null (or does not exists) 
//  // the blow default value will be present
//  public boolean hasSecurityBug = false;
//  public Double length = 123.45;
//  @AlsoLoad("specialOfferText")
//  public String specialOfferTextV2;
//  public String anotherString = "MyOtherStrin";
//}
