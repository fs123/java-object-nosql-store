package ch.ivyteam.neo4j;

import java.util.Map;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import com.google.gson.Gson;

public class JsonMapConverter implements AttributeConverter<Map<String,Object>, String>
{

  @SuppressWarnings("unchecked")
  @Override
  public Map<String,Object> toEntityAttribute(String json)
  {
    return new Gson().fromJson(json, Map.class);
  }

  @Override
  public String toGraphProperty(Map<String,Object> object)
  {
    return new Gson().toJson(object);
  }
  
}
