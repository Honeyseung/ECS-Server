import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<DataParser> getList() throws IOException, SAXException, JAXBException {
        ResourcePatternResolver resourcePatternResolver =
                new PathMatchingResourcePatternResolver(Server.class.getClassLoader());

        Resource[] resources = resourcePatternResolver.getResources("file:" + "/Users/hyunseung/Documents/IntelliJProject/ECS-Server/src/messages" + "/*.xml");

        Schema schema =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                        .newSchema(
                                new StreamSource(
                                        resourcePatternResolver
                                                .getResource("classpath:interface/message.xsd")
                                                .getInputStream()));

        if (resources.length > 0) {
            JAXBContext jaxbContext = JAXBContext.newInstance(config.Message.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            List<DataParser> list = new ArrayList<>();
            for (Resource resource : resources) {
                try {
                    unmarshaller.setSchema(schema);

                    config.Message message =
                            (config.Message)
                                    unmarshaller.unmarshal(resource.getInputStream());

                    list.add(new DataParser(message.getCode(), message.getData().getTypes()));
                    return list;
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws IOException, SAXException, JAXBException {
        final Logger logger = LoggerFactory.getLogger(Server.class);

        Config config = ConfigFactory.load();
        String configPath = System.getProperty("config.file");
        //CmsParser cmsParser = new CmsParser();
        //cmsParser.setup("/Users/hyunseung/Documents/IntelliJProject/ECS-Server/src/messages");
        if (configPath != null) {
            File f = new File(configPath);
            if (f.exists() && !f.isDirectory()) {
                config = ConfigFactory.parseFile(f).resolve();
                logger.info("Configuration is loaded. [{}]", f);
            } else {
                logger.error("Failed to load configuration. Please check the [-Dconfig.file] option.");
                System.exit(0);
            }
        } else {
            logger.debug("Configuration is loaded. (Development Mode)");
        }
        InetAddress address = InetAddress.getByName(config.getString("host"));
        System.out.println(address.getHostAddress()+" 연결");
        List<Integer> portList = config.getIntList("portList");
        Thread[] multiThreads = new Thread[portList.size()];
        MulticastSocket[] sockets = new MulticastSocket[portList.size()];
        for (int i = 0; i < portList.size(); i++) {
            int port = portList.get(i);
            int fnum = i;
            System.out.println(port + "포트 설정 중....");
            sockets[fnum] = new MulticastSocket(port);
            System.out.println(portList.get(fnum) + "포트 설정 완료");

            multiThreads[i] =
                    new Thread(
                            () -> {
                                System.out.println(port+"포트 스레드 시작");
                                while (true) {
                                    byte[] bytes = new byte[256];
                                    DatagramPacket msg = new DatagramPacket(bytes, bytes.length, address, port);
                                    try {
                                        sockets[fnum].receive(msg);
                                        System.out.println(port + "port Send: " + javax.xml.bind.DatatypeConverter.printHexBinary(msg.getData()) + " " + msg.getLength() + "\n");
                                        ByteBuf byteBuf = Unpooled.buffer(0);
                                        byteBuf.writeBytes(msg.getData());
                                        MessageData decoder=new MessageData();
                                        MessageData.Header header=new MessageData.Header();
                                        decoder.setHeader(header);
                                        decoder.getHeader().setMessageIdentifier(byteBuf.readInt());
                                        decoder.getHeader().setMessageSender(byteBuf.readShort());
                                        decoder.getHeader().setMessageLength(byteBuf.readShort());
                                        decoder.getHeader().setTimeSec(byteBuf.readInt());
                                        decoder.getHeader().setTimeMicrosec(byteBuf.readInt());
                                        byte[] data = new byte[byteBuf.readableBytes()];
                                        byteBuf.readBytes(data);
                                        decoder.setData(data);
                                        //JsonObject jsonObject = cmsParser.parse(header.getMessageIdentifier(), data);
                                        //System.out.println("Parsed Data : " + jsonObject+"\n");
                                        System.out.println("port: "+port+" | "+header+" | data: ["+ javax.xml.bind.DatatypeConverter.printHexBinary(data)+"] "+data.length);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
            multiThreads[i].start();
        }
    }
}

