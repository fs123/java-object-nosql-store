package ch.ivyteam.fintech;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

@Entity("dossiers")
@Indexes({
  @Index(options = @IndexOptions(name = "t3_queryComplexDossierSingleIndex") , 
  	fields = {
  			@Field("beneficialOwners.person.firstName"),
  			@Field("controllingPersonMgmt.fiduciaryHoldingAssets")
  			// MongoCommandException: Command failed with error 10088: 'cannot index parallel arrays [controllingPersons] [beneficialOwners]'
  			//,@Field("controllingPersonMgmt.controllingPersons.share")
  			} 
  ),
	
  @Index(options = @IndexOptions(name = "t3_queryComplexDossierIndexOnEachField_A") , 
		fields = @Field("beneficialOwners.person.lastName")),
	@Index(options = @IndexOptions(name = "t3_queryComplexDossierIndexOnEachField_B") , 
			fields = @Field("legitimateMgmt.onlyLegitimatesAreBO")),
//	@Index(options = @IndexOptions(name = "t3_queryComplexDossierIndexOnEachField_C") , 
//			fields = @Field("controllingPersonMgmt.controllingPersons.address.zipCode")),
//
//  @Index(options = @IndexOptions(name = "t3_queryComplexDossierIndexOnEachField_AB") , 
//		fields = {
//				@Field("beneficialOwners.person.lastName"),
//			  @Field("legitimateMgmt.onlyLegitimatesAreBO")
//			  }
//  ),
//  @Index(options = @IndexOptions(name = "t3_queryComplexDossierIndexOnEachField_BC") , 
//		fields = {
//			  @Field("legitimateMgmt.onlyLegitimatesAreBO"),
//			  @Field("controllingPersonMgmt.controllingPersons.share")
//			  }
//  		)
})
public class Dossier extends AddToStringHashCodeAndEquals
{
  @Id
  public ObjectId id;

  public AccountHolder accountHolder;
  public Set<BeneficialOwner> beneficialOwners = new HashSet<>();
  public ControllingPersonManagement controllingPersonMgmt;
  public LegitimateManagement legitimateMgmt;
}
