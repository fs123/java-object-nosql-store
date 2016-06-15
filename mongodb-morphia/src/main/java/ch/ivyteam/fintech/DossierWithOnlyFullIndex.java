package ch.ivyteam.fintech;

import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.utils.IndexType;

import ch.ivyteam.shop.model.v1.AddToStringHashCodeAndEquals;

@Entity("DossiersWithOnlyFullIndex")
@Indexes({
	@Index(fields = @Field(value = "$**", type = IndexType.TEXT)),
})
public class DossierWithOnlyFullIndex extends AddToStringHashCodeAndEquals
{
  @Id
  public ObjectId id;

  public AccountHolder accountHolder;
  public Set<BeneficialOwner> beneficialOwners = new HashSet<>();
  public ControllingPersonManagement controllingPersonMgmt;
  public LegitimateManagement legitimateMgmt;
}
