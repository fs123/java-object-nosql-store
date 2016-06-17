package ch.ivyteam.orient.fintech;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;

import ch.ivyteam.orient.fintech.AccountHolder;
import ch.ivyteam.orient.fintech.BeneficialOwner;
import ch.ivyteam.orient.fintech.ControllingPerson;
import ch.ivyteam.orient.fintech.ControllingPersonManagement;
import ch.ivyteam.orient.fintech.Dossier;
import ch.ivyteam.orient.fintech.Legitimate;
import ch.ivyteam.orient.fintech.LegitimateManagement;
import ch.ivyteam.orient.fintech.Person;

public class RandomDossier
{

  public static Dossier generate()
  {
    Dossier dossier = new Dossier();
    dossier.setAccountHolder(createAccountHolder());
    dossier.setBeneficialOwners(createBeneficialOwners(dossier));
    dossier.setControllingPersonMgmt(controllingMgmt(dossier));
    dossier.setLegitimateMgmt(legitimateMgmt(dossier));
    return dossier;
  }

  private static LegitimateManagement legitimateMgmt(Dossier dossier)
  {
    LegitimateManagement mgmt = new LegitimateManagement();
    mgmt.setOnlyLegitimatesAreBO(RandomUtils.nextInt(0, 2) == 0);

    BeneficialOwner benifician = randomOf(dossier.getBeneficialOwners());
    Legitimate copy = new Legitimate();
    copy.setPerson(benifician.getPerson());
    copy.setAddress(benifician.getAddress());
    copy.setContact(Contacts.createRandom());

    for(int i=0; i<RandomUtils.nextInt(0, 3); i++)
    {
      Legitimate random = new Legitimate();
      random.setPerson(randomPerson());
      random.setAddress(Addresses.getRandom());
      random.setContact(Contacts.createRandom());
      mgmt.getLegitimates().add(random);
    }

    return mgmt;
  }

  private static ControllingPersonManagement controllingMgmt(Dossier dossier)
  {
    ControllingPersonManagement mgmt = new ControllingPersonManagement();

    BeneficialOwner benifician = randomOf(dossier.beneficialOwners);
    ControllingPerson copied = new ControllingPerson();
    copied.person = benifician.person;
    copied.address = benifician.address;
    copied.share = RandomUtils.nextInt(0, 100);

    for(int i=0; i<RandomUtils.nextInt(0, 2); i++)
    {
      ControllingPerson random = new ControllingPerson();
      random.person = randomPerson();
      random.address = Addresses.getRandom();
      random.share = RandomUtils.nextInt(0, 100);
      mgmt.controllingPersons.add(random);
    }

    mgmt.controllingPersons.add(copied);
    return mgmt;
  }

  private static <T> T randomOf(Collection<T> entities)
  {
    if (entities == null || entities.size() == 0)
    {
      return null;
    }
    List<T> listified = new ArrayList<>(entities);
    int entry = RandomUtils.nextInt(0, entities.size());
    return listified.get(entry);
  }

  private static AccountHolder createAccountHolder()
  {
    AccountHolder holder = new AccountHolder();
    holder.name = Names.randomFullName();
    holder.uidNumber = UUID.randomUUID().toString();
    holder.dateOfRegistry = randomDate();
    holder.dateOfSupply = randomDate();
    holder.yearOfIncorporation = RandomUtils.nextInt(1900, 2016);
    holder.numberOfEmployees = RandomUtils.nextInt(1, 100_000);
    holder.contactPerson = Names.randomFullName();
    holder.domicileCountryControllingPerson = Names.randomFullName();
    holder.address = Addresses.getRandom();
    return holder;
  }

  private static Set<BeneficialOwner> createBeneficialOwners(Dossier dossier)
  {
    Set<BeneficialOwner> benificial = new HashSet<>();
    for(int i=0; i<RandomUtils.nextInt(2, 5); i++)
    {
      benificial.add(createBeneficialOwner(dossier));
    }
    return benificial;
  }

  private static BeneficialOwner createBeneficialOwner(Dossier dossier)
  {
    BeneficialOwner beneficial = new BeneficialOwner();
    beneficial.person = randomPerson();
    beneficial.address = Addresses.getRandom();
    return beneficial;
  }

  private static Person randomPerson()
  {
    Person person = new Person();
    person.title = Names.randomTitle();
    person.firstName = Names.getRandomFirstname();
    person.lastName = Names.getRandomLastname();
    person.birthDate = randomDate();
    return person;
  }

  public static Date randomDate()
  {
    long currentTimeMillis = System.currentTimeMillis();
    return new Date(Math.abs(currentTimeMillis - RandomUtils.nextLong(0, currentTimeMillis)));
  }

}
