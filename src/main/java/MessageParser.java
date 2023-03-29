import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import interfaces.RawData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class MessageParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Logger rawDataRecorder = LoggerFactory.getLogger("DisRawDataRecorder");
    private final Gson gson = new GsonBuilder().serializeNulls().create();
    private int code;

    MessageParser(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public JsonObject parse(ByteBuf rawData) {
        MessageData message = new MessageData();
        ByteBuf byteBuf = null;
        JsonObject jsonObject = null;
        try {
            byteBuf = Unpooled.wrappedBuffer(message.getData());
            System.out.println("Parser: " + message.getHeader().getMessageIdentifier() + " " + message.getData().length);
            String hex = ByteBufUtil.hexDump(byteBuf);

            jsonObject = parse(message.getHeader(), byteBuf);


            jsonObject.addProperty("raw", "0x" + hex);

            rawDataRecorder.info(
                    "Header : {}, Data : {}",
                    message.getHeader(),
                    gson.toJson(jsonObject));
        } catch (Exception e) {
            logger.error("parse", e);

        } finally {
            ReferenceCountUtil.release(byteBuf);
        }

        return jsonObject;
    }

    public abstract JsonObject parse(
            MessageData.Header header, ByteBuf byteBuf) throws Exception;

    public abstract Optional<Object> parse(RawData rawData);
}
