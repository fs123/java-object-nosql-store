package ch.ivyteam.fintech;

import java.util.ArrayList;
import java.util.List;

public class Addresses
{
  private static List<String> addresses; 

	private static List<String> read; 

  private static synchronized List<String> read()
  {
  	// downloaded from https://results.openaddresses.io/
  	if (read == null) {
  		read = CsvReader.read("jura.csv");
  	}
  	return new ArrayList<>(read);
  }
  
  public static synchronized Address getRandom()
  {
    if (addresses == null)
    {
      addresses = read();
    }
    if (addresses == null)
    {
      return null;
    }
    if (addresses.isEmpty())
    { // all used re-read 
      addresses = read();
    }
    int entry = (int) Math.random() * (addresses.size()-1);
    String address = addresses.remove(entry);
    return toObject(address);
  }
  
  private static Address toObject(String adrLine)
  {
    String[] columns = adrLine.split(",");
    Address address = new Address();
    address.street = columns[COLUMN.STREET] + " " + columns[COLUMN.NUMBER];
    address.zipCode = columns[COLUMN.ZIP];
    address.city = columns[COLUMN.CITY];
    address.country = "France";
    return address;
  }
  
  private static interface COLUMN
  {
    int NUMBER = 2;
    int STREET = 3;
    int CITY = 5;
    int ZIP = 8;
  }
  
}
