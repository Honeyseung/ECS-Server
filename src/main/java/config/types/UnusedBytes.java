package config.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unusedBytes")
public class UnusedBytes extends Type {
  @XmlAttribute(name = "length", required = true)
  protected int length;

  public int getLength() {
    return length;
  }
  
}
