package ch.ivyteam.shop.model.v1;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AddToStringHashCodeAndEquals
{

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
  
  @Override
  public int hashCode()
  {
    return HashCodeBuilder.reflectionHashCode(this);
  }
  
  @Override
  public boolean equals(Object obj)
  {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}
