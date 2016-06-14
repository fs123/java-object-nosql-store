//package ch.ivyteam.shop.model.v2;
//
//import java.util.Date;
//
//import org.bson.types.ObjectId;
//import org.mongodb.morphia.annotations.Embedded;
//import org.mongodb.morphia.annotations.Entity;
//import org.mongodb.morphia.annotations.Field;
//import org.mongodb.morphia.annotations.Id;
//import org.mongodb.morphia.annotations.Index;
//import org.mongodb.morphia.annotations.IndexOptions;
//import org.mongodb.morphia.annotations.Indexes;
//import org.mongodb.morphia.annotations.PrePersist;
//import org.mongodb.morphia.annotations.Property;
//import org.mongodb.morphia.annotations.Reference;
//
//import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;
//import ch.ivyteam.shop.model.v1.Vendor;
//
///**
// * Example:
// * <pre>
// * {  
// *   "_id":ObjectId("57594592e6090a2b74be5557"),
// *   "className":"ch.ivyteam.shop.model.v1.Product",
// *   "name":"Müesli",
// *   "product_price":66,
// *   "options":{  
// *      "hasSecurityBug":false,
// *      "hasTurboBooster":true,
// *      "specialOfferText":"Today with choco ships",
// *      "length":123.45
// *   },
// *   "ventor":DBRef("vendors", ObjectId("57594592e6090a2b74be5556"))
// * }
// */
//
//// Any class annotated with @Entity is treated as a top level document stored
//// directly in a collection<p>
//// see
//// http://mongodb.github.io/morphia/1.2/guides/annotations/#entity:338aacf6337d00fc0cde843c3182c3f8
//@Entity("products")
//
//@Indexes({
//  @Index(options = @IndexOptions(name = "product_name") , fields = @Field("name") ),
//  @Index(options = @IndexOptions(name = "product_vendor") , fields = @Field("vendor") )
//  })
//public class ProductV2 extends AddToStringHashCodeAndEquals
//{
//  public static ProductV2 create(String name, int price, Vendor vendor, String offerText)
//  {
//    ProductV2 product = new ProductV2();
//    product.name = name;
//    product.price = price;
//    product.vendor = vendor;
//    product.options = new ProductOptionsV2();
//    product.options.anotherString = offerText;
//    return product;
//  }
//  
//  // Any class with @Entity must have a field annotated with @Id to define which
//  // field to use as the _id value in the document written to MongoDB
//  @Id
//  public ObjectId id;
//
//  public String name;
//
//  // lets change the field name
//  @Property("product_price")
//  public int price;
//
//  // @Embedded indicates that the class will result in a subdocument inside
//  // another document. @Embedded classes do not require the presence of an @Id
//  // field.
//  @Embedded
//  public ProductOptionsV2 options;
//  @Embedded
//  public ProductOptionsV2 weekendOptions;
//  
//  public Date lastUpdated;
//  
//  @PrePersist void prePersist()
//  {
//    lastUpdated = new Date();
//  }
//  
//  // This field refers to other Morphia mapped entities.
//  @Reference
//  public Vendor vendor;
//
//  public ObjectId getId()
//  {
//    return id;
//  }
//
//  public void setId(ObjectId id)
//  {
//    this.id = id;
//  }
//
//  public String getName()
//  {
//    return name;
//  }
//
//  public void setName(String name)
//  {
//    this.name = name;
//  }
//
//  public int getPrice()
//  {
//    return price;
//  }
//
//  public void setPrice(int price)
//  {
//    this.price = price;
//  }
//
//  public ProductOptionsV2 getOptions()
//  {
//    return options;
//  }
//
//  public void setOptions(ProductOptionsV2 options)
//  {
//    this.options = options;
//  }
//
//  public Vendor getVentor()
//  {
//    return vendor;
//  }
//
//  public void setVentor(Vendor ventor)
//  {
//    this.vendor = ventor;
//  }
//  
//}
