import config.HyunMessage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Objects;

public class HyunParser {
    public void marshalTest() {
        try {
            HyunMessage hyunMessage = new HyunMessage(1, 20);

            JAXBContext context = JAXBContext.newInstance(HyunMessage.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(hyunMessage, System.out);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void unmarshalTest(){
        try{
            File file = new File("src/messages/hyun.xml");
            System.out.println(file.getName());
            JAXBContext context = JAXBContext.newInstance(HyunMessage.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            HyunMessage hyunMessage=(HyunMessage) unmarshaller.unmarshal(file);

            System.out.println("identifier: "+hyunMessage.getIdentifier());
            System.out.println("quantity: "+hyunMessage.getQuantity());
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public static void  main(String[] args) {
        //new HyunParser().marshalTest();
        new HyunParser().unmarshalTest();
    }
}
