package ch.ivyteam.fintech;

import org.apache.commons.lang3.RandomUtils;

public class Correspondences
{

  public Correspondence createRandom()
  {
    Correspondence correspondence = new Correspondence();
    correspondence.title = Names.randomTitle();
    correspondence.firstName = Names.getRandomFirstname();
    correspondence.lastName = Names.getRandomLastname();
    correspondence.differentAddress = RandomUtils.nextInt(0,2)==0;
    if (correspondence.differentAddress)
    {
      correspondence.address = Addresses.getRandom();
    }
    correspondence.contact = Contacts.createRandom();
    return correspondence;
  }
  
}
