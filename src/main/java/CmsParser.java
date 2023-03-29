import com.google.gson.JsonObject;
import interfaces.Message;
import interfaces.ParsedData;
import interfaces.RawData;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CmsParser {
    private Map<Integer, MessageParser> messageParsers = new HashMap<>();

    public void setup(String messagesPath) {
        try {
            ResourcePatternResolver resourcePatternResolver =
                    new PathMatchingResourcePatternResolver(getClass().getClassLoader());

            Resource[] resources =
                    resourcePatternResolver.getResources("file:" + messagesPath + "/*.xml");

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


                for (Resource resource : resources) {
                    try {
                        unmarshaller.setSchema(schema);

                        config.Message message =
                                (config.Message)
                                        unmarshaller.unmarshal(resource.getInputStream());
                        System.out.println("message: "+message.getCode()+" "+message.getData());
                        messageParsers.put(
                                message.getCode(),
                                new DataParser(message.getCode(), message.getData().getTypes()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonObject parse(int messageCode, byte[] bytes) throws Exception {
        MessageParser messageParser = messageParsers.get(messageCode);

        if (messageParser == null) {
            throw new Exception("존재하지 않는 code: "+messageCode);
        }

        Message message =
                (Message) messageParser
                        .parse(
                                new RawData() {
                                    @Override
                                    public Optional<String> deviceId() {
                                        return Optional.empty();
                                    }

                                    @Override
                                    public Optional<String> sensorNodeId() {
                                        return Optional.empty();
                                    }

                                    @Override
                                    public Optional<Object> rawData() {
                                        MessageData message = new MessageData();
                                        MessageData.Header header = new MessageData.Header();
                                        header.setMessageIdentifier(messageCode);

                                        message.setHeader(header);
                                        message.setData(bytes);

                                        return Optional.of(message);
                                    }
                                })
                        .orElseThrow(() -> new Exception("Parse Error"));

        return ((ParsedData) message).data().orElseThrow(() -> new Exception("Cast Error"));
    }
}
