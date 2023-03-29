import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

@Data
public class MessageData {
    private Header header;
    private byte[] data;

    public MessageData() {
    }

    @Data
    public static class Header {
        int messageIdentifier;
        short messageSender;
        short messageLength;
        int timeSec;
        int timeMicrosec;

        public String toString(){
            return "Header: [Identifier="+messageIdentifier+", "+
                    "Sender="+messageSender+", "+
                    "Length="+messageLength+", "+
                    "Sec="+timeSec+", "+
                    "Microsec="+timeMicrosec+"]";
        }
    }
}
