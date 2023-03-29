package config;

import config.types.Float;
import config.types.Int;
import config.types.Short;
import config.types.Type;
import config.types.Double;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class HyunMessage {
    @XmlElement
    private int identifier;
    @XmlElement
    private int quantity;

    protected Data data;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "data")
    public static class Data {

        @XmlElements({
                @XmlElement(name = "double", type = Double.class),
                @XmlElement(name = "short", type = Short.class),
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

    public HyunMessage(int i, int q) {
        identifier = i;
        quantity = q;
    }

    public HyunMessage() {
    }
}
