package ch.ivyteam.java.object.store.data;

import java.util.List;

public class Person
{
  private static List<String> firstnames;

  public static String getRandomFirstname()
  {
    if (firstnames == null)
    {
      firstnames = CsvReader.read("firstnames.csv");
      // http://www.quietaffiliate.com/free-first-name-and-last-name-databases-csv-and-sql/
    }
    if (firstnames == null)
    {
      return null;
    }
    return randomOf(firstnames);
  }

  public static String randomOf(List<String> lines)
  {
    int entry = (int)Math.round(Math.random() * (lines.size()-1));
    return lines.get(entry);
  }
  
  private static List<String> lastnames;

  public static String getRandomLastname()
  {
    if (lastnames == null)
    {
      lastnames = CsvReader.read("lastnames.csv");
      // http://www.quietaffiliate.com/free-first-name-and-last-name-databases-csv-and-sql/
    }
    if (lastnames == null)
    {
      return null;
    }
    return randomOf(lastnames);
  }
  
}
