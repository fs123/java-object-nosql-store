//package ch.ivyteam.shop;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.mongodb.morphia.Datastore;
//import org.mongodb.morphia.Morphia;
//import org.mongodb.morphia.query.Query;
//
//import com.mongodb.MongoClient;
//
//import ch.ivyteam.shop.model.v1.Product;
//import ch.ivyteam.shop.model.v1.Vendor;
//import ch.ivyteam.shop.model.v1.Vendor.RATING;
//import ch.ivyteam.shop.model.v2.ProductV2;
//
//public class ModelChangeTest
//{
//
//  private Datastore datastore;
//
//  @Before
//  public void init()
//  {
//    final Morphia morphia = new Morphia();
//
//    // tell Morphia where to find your classes
//    // can be called multiple times with different packages or classes
//    morphia.mapPackage("ch.ivyteam.college");
//
//    // Mapping options:
//    // morphia.getMapper(). ...
//    // -By default Morphia will not store empty List or Map values 
//    //  nor will it store null values in to MongoDB.
//    datastore = morphia.createDatastore(new MongoClient(), "SimpleCrudOperations");
//    datastore.ensureIndexes();
//  }
//
//  @After
//  public void cleanup()
//  {
//    datastore.getDB().dropDatabase();
//  }
//  
//  @Test
//  @Ignore
//  public void testWriteOldReadNew()
//  {
//    // CREATE
//    Vendor vendorA = Vendor.create("Steep and Cheap", RATING.OK);
//    
//    Product pa1 = Product.create("PA1 Bali and back", 11, vendorA, "PA1");
//    Product pa2 = Product.create("PA2 USA and hack", 22, vendorA, "PA2");
//    
//    datastore.save(vendorA, pa1, pa2);
//
//    Query<ProductV2> readNew = datastore.find(ProductV2.class);
//    List<ProductV2> newProducts = readNew.asList();
//    assertThat(newProducts).hasSize(2);
//    
//    ProductV2 newProduct = datastore.find(ProductV2.class, "id", pa1.id).get();
//    
//    assertThat(pa1.name).isEqualTo(newProduct.name);
//    assertThat(pa1.options.specialOfferText).isEqualTo(newProduct.options.specialOfferTextV2);
//    
//    
//  }
//}
