package ch.ivyteam.shop.model.v1;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * <pre>
 * > db.vendors.find() 
 * { 
 *   "_id" : ObjectId("57594592e6090a2b74be5556"),
 *   "className" : "ch.ivyteam.shop.model.v1.Vendor", 
 *   "companyName" : "Steep and Cheap", 
 *   "rating" : "TOP" 
 * }
 */
@Entity("vendors")
public class Vendor extends AddToStringHashCodeAndEquals
{

  public static Vendor create(String name, RATING rating)
  {
    Vendor vendor = new Vendor();
    vendor.companyName = name;
    vendor.rating = rating;
    return vendor;
  }
  
  public static enum RATING
  {
    TOP, OK, STOP
  }

  @Id
  public ObjectId id;

  public String companyName;
  public RATING rating;

  public String getCompanyName()
  {
    return companyName;
  }

  public void setCompanyName(String companyName)
  {
    this.companyName = companyName;
  }

  public RATING getRating()
  {
    return rating;
  }

  public void setRating(RATING rating)
  {
    this.rating = rating;
  }
}
