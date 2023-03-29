import lombok.Data;

@Data
public class Header {
    int messageIdentifier;
    short messageSender;
    short messageLength;
    int timeSec;
    int timeMicrosec;
    public String toString(){
        return "{Header: [Identifier= "+messageIdentifier+", "+
                "Sender= "+messageSender+", "+
                "Length= "+messageLength+", "+
                "Sec= "+timeSec+", "+
                "Microsec= "+timeMicrosec+"]}";
    }
}
