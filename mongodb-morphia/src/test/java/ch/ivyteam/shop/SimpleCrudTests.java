package ch.ivyteam.shop;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.MongoClient;

import ch.ivyteam.shop.model.v1.Product;
import ch.ivyteam.shop.model.v1.ProductOptions;
import ch.ivyteam.shop.model.v1.Vendor;
import ch.ivyteam.shop.model.v1.Vendor.RATING;

public class SimpleCrudTests
{

  private Datastore datastore;

  @Before
  public void init()
  {
    final Morphia morphia = new Morphia();

    // tell Morphia where to find your classes
    // can be called multiple times with different packages or classes
    morphia.mapPackage("ch.ivyteam.college");

    // Mapping options:
    // morphia.getMapper(). ...
    // -By default Morphia will not store empty List or Map values 
    //  nor will it store null values in to MongoDB.
    datastore = morphia.createDatastore(new MongoClient(), "SimpleCrudOperations");
    datastore.ensureIndexes();
  }

  @After
  public void cleanup()
  {
    datastore.getDB().dropDatabase();
  }
  
  @Test
  public void showSimpleCrudOperations()
  {
    Vendor vendor = Vendor.create("Steep and Cheap", RATING.OK);
    
    Product p1 = Product.create("Müesli", 11, vendor, "Today with choco chips!");
    // if the message is null, equals will fail!
    Product p2 = Product.create("Rüebli", 22, vendor, "");
    Product p3 = Product.create("Öpfel", 33, vendor, "àüö{}[]\"+*ç%&/()=");
    
    // Saves the entities (Objects) and updates the @Id field
    datastore.save(vendor, p1, p2, p3);
    
    // READ
    final Query<Product> query = datastore.createQuery(Product.class);
    assertThat(query.asList()).contains(p1, p2, p3);

    // FIND by name
    Query<Product> ruebli = datastore.createQuery(Product.class)
      .field("name")
      .contains("ües");
    assertThat(ruebli.asList()).contains(p1);
    
    // UPDATE
    final Query<Product> underPaidQuery = datastore.createQuery(Product.class)
            .filter("price <=", 11);
    final UpdateOperations<Product> updateOperations = datastore.createUpdateOperations(Product.class)
            .inc("price", 55)
            .set("options.specialOfferText", "Tomorrow with fresh fruits");
    
    final UpdateResults results = datastore.update(underPaidQuery, updateOperations);
    
    assertThat(results.getUpdatedCount()).isEqualTo(1);
    
    Product updatedMuesli = datastore.createQuery(Product.class).filter("id", p1.id).get();
    assertThat(updatedMuesli.price).isEqualTo(66);
    assertThat(updatedMuesli.options.specialOfferText).isEqualTo("Tomorrow with fresh fruits");
    
    // DELETE
    assertThat(datastore.getCount(Product.class)).isEqualTo(3);
    datastore.delete(p2);
    assertThat(datastore.getCount(Product.class)).isEqualTo(2);
  }
  
  @Test
  public void testReferencesAreNotTheSame()
  {
    // STORE same references
    Vendor vendor = Vendor.create("Steep and Cheap", RATING.OK);
    Product p1 = Product.create("Öpfel", 33, vendor, "àüö{}[]\"+*ç%&/()=");
    Product p2 = Product.create("Rüebli", 22, vendor, "");
    p1.weekendOptions = p1.options;
    p2.weekendOptions = p1.options;
    
    datastore.save(vendor, p1, p2);
    
    assertThat(p1.vendor).isSameAs(vendor);
    assertThat(p2.vendor).isSameAs(vendor);
    assertThat(p1.vendor).isSameAs(p2.vendor);
    
    assertThat(p1.weekendOptions).isSameAs(p1.options);
    assertThat(p2.weekendOptions).isSameAs(p1.options);
    assertThat(p1.weekendOptions).isSameAs(p2.weekendOptions);
    
    
    // READ them => they will no longer be the same references!
    Vendor readVendor = datastore.find(Vendor.class, "id", vendor.id).get();
    Product readP1 = datastore.find(Product.class, "id", p1.id).get();
    Product readP2 = datastore.find(Product.class, "id", p2.id).get();
    
    assertThat(readP1.vendor).isNotSameAs(readVendor);
    assertThat(readP2.vendor).isNotSameAs(readVendor);
    assertThat(readP1.vendor).isNotSameAs(p2.vendor);
    
    assertThat(readP1.weekendOptions).isNotSameAs(readP1.options);
    assertThat(readP2.weekendOptions).isNotSameAs(readP1.options);
    assertThat(readP1.weekendOptions).isNotSameAs(readP2.weekendOptions);
    
    
    // two queries => NOT the same object
    assertThat(readP1.vendor).isNotSameAs(readVendor);
    assertThat(readP2.vendor).isNotSameAs(readVendor);
    
    // queried in the same query => SAME OBEJCT!
    List<Product> products = datastore.find(Product.class).asList();
    assertThat(products.get(0).vendor).isSameAs(products.get(1).vendor);
    
    // event two read will NOT return the same object (there is no cache like in JPA/Hibernate)
    Product read2P1 = datastore.find(Product.class, "id", p1.id).get();
    assertThat(readP1).isNotSameAs(read2P1);
  }

  @Test
  public void simpleJoinedSearch()
  {
    // CREATE
    Vendor vendorA = Vendor.create("Steep and Cheap", RATING.OK);
    Vendor vendorB = Vendor.create("Hot and Flop", RATING.TOP);
    
    Product pa1 = Product.create("PA1 Bali and back", 11, vendorA, "PA1");
    Product pa2 = Product.create("PA2 USA and hack", 22, vendorA, "PA2");

    Product pb1 = Product.create("PB1 Wellness", 33, vendorB, "PB1");
    Product pb2 = Product.create("PB2 Massage", 44, vendorB, "PB2");
    
    datastore.save(vendorA, vendorB, pa1, pa2, pb1, pb2);

    // Faked join 
    // - inspect exec. query with filter1.explain() -> currently a COLLSCAN was exectued -> BAD performance!) 
    Query<Product> filter1 = datastore
              .createQuery(Product.class)
              .filter("vendor", vendorA);
    assertThat(filter1.asList()).contains(pa1, pa2);
    assertThat(QueryHelper.doesUseIndex(filter1)).isEqualTo(false);
    
    // Faked join 2
    Query<Vendor> ratingFilter = datastore.find(Vendor.class, "rating", RATING.TOP);
    Query<Product> filter2 = datastore.find(Product.class).field("vendor").in(ratingFilter);
    assertThat(filter2.asList()).contains(pb1, pb2);
    assertThat(QueryHelper.doesUseIndex(filter2)).isEqualTo(false);
  }
  
  @Test
  public void testUpdateInDifferentPlacesIsMerged()
  {
    // STORE same references
    Vendor vendor = Vendor.create("Steep and Cheap", RATING.OK);
    Product p1 = Product.create("Öpfel", 33, vendor, "Option");
    p1.weekendOptions = new ProductOptions();
    p1.weekendOptions.specialOfferText = "Special Offer";

    datastore.save(vendor, p1);

    Product readP11 = datastore.find(Product.class, "id", p1.id).get();
    Product readP12 = datastore.find(Product.class, "id", p1.id).get();

    assertThat(readP11).isNotSameAs(readP12);

    readP11.options.specialOfferText = "New Text";
    readP12.weekendOptions.specialOfferText = "New Text Weekend";

    UpdateOperations<Product> updateOperations = datastore.createUpdateOperations(Product.class).set("options", readP11.options);
    datastore.update(readP11, updateOperations);

    UpdateOperations<Product> updateOperations2 = datastore.createUpdateOperations(Product.class).set("weekendOptions", readP12.weekendOptions);
    datastore.update(readP12, updateOperations2);

    readP11 = datastore.find(Product.class, "id", p1.id).get();
    assertThat(readP11.options.specialOfferText).isEqualTo("New Text");
    assertThat(readP11.weekendOptions.specialOfferText).isEqualTo("New Text Weekend");
  }

}
