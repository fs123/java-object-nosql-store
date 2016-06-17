package ch.ivyteam.orient.fintech;

import org.apache.commons.lang3.RandomStringUtils;

import ch.ivyteam.orient.fintech.Contact;

public class Contacts
{

  public static Contact createRandom()
  {
    Contact c = new Contact();
    c.setBusinessPhone(randomPhone());
    c.setLandlinePhone(randomPhone());
    c.setMobilePhone(randomPhone());
    c.seteMail(randomMail());
    return c;
  }

  public static String randomMail()
  {
    return RandomStringUtils.random(1, true, false) + "." + Names.getRandomLastname()+"@"+RandomStringUtils.random(5)+".com";
  }

  public static String randomPhone()
  {
    return RandomStringUtils.random(12, false, true);
  }

}
