package config;

import config.types.Boolean;
import config.types.Float;
import config.types.Int;
import config.types.Type;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "",
    propOrder = {"code", "data"})
@XmlRootElement(name = "message")
public class Message {

  @XmlElement(required = true)
  private int code;

  protected Data data;

  public int getCode() {
    return code;
  }

  public Data getData() {
     return data;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(
      name = "data",
      propOrder = {"types"})
  public static class Data {

    @XmlElements({
      //@XmlElement(name = "boolean", type = Boolean.class),
      @XmlElement(name = "int", type = Int.class),
      @XmlElement(name = "float", type = Float.class),
    })
    protected List<Type> types;

    public List<Type> getTypes() {
      if (types == null) {
        types = new ArrayList<>();
      }
      return this.types;
    }
  }
}
