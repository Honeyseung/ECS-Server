import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import interfaces.RawData;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import config.types.Boolean;
import config.types.Byte;
import config.types.Double;
import config.types.Float;
import config.types.Long;
import config.types.Short;
import config.types.*;

import java.nio.charset.Charset;
import java.util.List;
import java.lang.String;
import java.util.Optional;

public class DataParser extends MessageParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private List<Type> types;
    private Config config;

    public DataParser(int code,List<Type> types) {
        super(code);
        this.types = types;
        this.config = ConfigFactory.load();
    }

    @Override
    public JsonObject parse(MessageData.Header header, ByteBuf byteBuf) throws Exception {
        JsonObject jsonObject = new JsonObject();
        for (Type type : types) {
            process(type, jsonObject, byteBuf);
        }
        return jsonObject;
    }

    @Override
    public Optional<Object> parse(RawData rawData) {
        return Optional.empty();
    }

    /**
     * @param type
     * @param jsonObject
     * @param byteBuf
     * @return
     * @throws Exception
     */
    public JsonObject process(Type type, JsonObject jsonObject, ByteBuf byteBuf) throws Exception {
        String key = ":";
        logger.debug("Parsing Tag Name : {}", key);

        if (type instanceof Float) {
            jsonObject.addProperty(key, byteBuf.readFloat());
        } else if (type instanceof FloatLE) {
            jsonObject.addProperty(key, byteBuf.readFloatLE());
        } else if (type instanceof Double) {
            jsonObject.addProperty(key, byteBuf.readDouble());
        } else if (type instanceof DoubleLE) {
            jsonObject.addProperty(key, byteBuf.readDoubleLE());
        } else if (type instanceof Int) {
            jsonObject.addProperty(key, byteBuf.readInt());
        } else if (type instanceof IntLE) {
            jsonObject.addProperty(key, byteBuf.readIntLE());
        } else if (type instanceof UnsignedInt) {
            jsonObject.addProperty(key, byteBuf.readUnsignedInt());
        } else if (type instanceof UnsignedIntLE) {
            jsonObject.addProperty(key, byteBuf.readUnsignedIntLE());
        } else if (type instanceof Long) {
            jsonObject.addProperty(key, byteBuf.readLong());
        } else if (type instanceof LongLE) {
            jsonObject.addProperty(key, byteBuf.readLongLE());
        } else if (type instanceof Short) {
            jsonObject.addProperty(key, byteBuf.readShort());
        } else if (type instanceof ShortLE) {
            jsonObject.addProperty(key, byteBuf.readShortLE());
        } else if (type instanceof UnsignedShort) {
            jsonObject.addProperty(key, byteBuf.readUnsignedShort());
        } else if (type instanceof UnsignedShortLE) {
            jsonObject.addProperty(key, byteBuf.readUnsignedShortLE());
        } else if (type instanceof Char) {
            jsonObject.addProperty(key, byteBuf.readChar());
        } else if (type instanceof Chars) {
            jsonObject.addProperty(key, (String) byteBuf.readCharSequence(((Chars) type).getLength(), Charset.forName("utf-8")));
        } else if (type instanceof Byte) {
            jsonObject.addProperty(key, byteBuf.readByte());
        } else if (type instanceof UnsignedByte) {
            jsonObject.addProperty(key, byteBuf.readUnsignedByte());
        } else if (type instanceof Boolean) {
            jsonObject.addProperty(key, byteBuf.readBoolean());
        } else {
            System.out.println("Unknown Tag! Tag Name : " + key);
        }

        return jsonObject;
    }
}
