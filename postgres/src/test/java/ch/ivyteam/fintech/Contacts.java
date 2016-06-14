package ch.ivyteam.fintech;

import org.apache.commons.lang3.RandomStringUtils;

public class Contacts
{

  public static Contact createRandom()
  {
    Contact c = new Contact();
    c.businessPhone = randomPhone();
    c.landlinePhone = randomPhone();
    c.mobilePhone = randomPhone();
    c.eMail = randomMail();
    return c;
  }

  public static String randomMail()
  {
    return RandomStringUtils.random(1, true, false) + "." + Names.getRandomLastname()+"@"+RandomStringUtils.randomAlphabetic(5)+".com";
  }
  
  public static String randomPhone()
  {
    return RandomStringUtils.random(12, false, true);
  }
  
}
