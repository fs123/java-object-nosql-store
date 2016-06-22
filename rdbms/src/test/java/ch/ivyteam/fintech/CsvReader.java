package ch.ivyteam.fintech;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.StopWatch;

public class CsvReader
{

  public static List<String> read(String file)
  {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    InputStream csvStream = Addresses.class.getResourceAsStream(file);
    try
    {
      List<String> lines = IOUtils.readLines(csvStream);
      lines.remove(0); // strip header
      stopWatch.stop();
      System.out.println("loaded "+file+" in "+stopWatch.getTime()+" ms");
      return lines;
    }
    catch (IOException ex)
    {
      return null;
    }
  }
  
}
