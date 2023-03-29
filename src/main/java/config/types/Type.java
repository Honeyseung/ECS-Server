package config.types;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type")
@XmlSeeAlso({
  String.class,
  Double.class,
  Float.class,
  Int.class,
  Boolean.class,
  Char.class,
  Time.class,
})
public class Type{

  @XmlAttribute(name = "name")
  protected String name;

  public String getName() {
    return name;
  }
}
