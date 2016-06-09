package ch.ivyteam.shop;

import java.util.Map;

import org.mongodb.morphia.query.Query;

import ch.ivyteam.shop.model.v1.Product;

public class QueryHelper
{
  @SuppressWarnings("unchecked")
  public static Boolean doesUseIndex(Query<Product> filter)
  {
    return (Boolean)((Map<String, Object>) filter.explain().get("queryPlanner")).get("indexFilterSet");
  }

}
