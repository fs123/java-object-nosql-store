package ch.ivyteam.orient.fintech;

import java.util.List;

import ch.ivyteam.orient.fintech.Address;

public class Addresses
{
  private static List<String> addresses;

  private static List<String> read()
  {
    return CsvReader.read("jura.csv");
    // downloaded from https://results.openaddresses.io/
  }

  public static Address getRandom()
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
    int entry = (int) Math.round(Math.random() * (addresses.size()-1));
    String address = addresses.remove(entry);
    return toObject(address);
  }

  private static Address toObject(String adrLine)
  {
    String[] columns = adrLine.split(",");
    Address address = new Address();
    address.setStreet(columns[COLUMN.STREET] + " " + columns[COLUMN.NUMBER]);
    address.setZipCode(columns[COLUMN.ZIP]);
    address.setCity(columns[COLUMN.CITY]);
    address.setCountry("France");
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
