package ch.ivyteam.orient.fintech;

import org.apache.commons.lang3.RandomUtils;

import ch.ivyteam.orient.fintech.Correspondence;

public class Correspondences
{

  public Correspondence createRandom()
  {
    Correspondence correspondence = new Correspondence();
    correspondence.setTitle(Names.randomTitle());
    correspondence.setFirstName(Names.getRandomFirstname());
    correspondence.setLastName(Names.getRandomLastname());
    correspondence.setDifferentAddress(RandomUtils.nextInt(0,2)==0);
    if (correspondence.isDifferentAddress())
    {
      correspondence.setAddress(Addresses.getRandom());
    }
    correspondence.setContact(Contacts.createRandom());
    return correspondence;
  }

}
