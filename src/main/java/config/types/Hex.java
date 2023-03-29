package config.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hex")
public class Hex extends Type {
  
  @XmlAttribute(name = "length", required = true)
  protected int length;
  
  @XmlAttribute(name = "endian")
  protected java.lang.String endian;
  
  @XmlAttribute(name = "format")
  protected java.lang.String format = "0x";
  
  public int getLength() {
    return length;
  }
  
  public java.lang.String getEndian() {
    return endian;
  }
  
  public java.lang.String getFormat() {
    return format;
  }
}
