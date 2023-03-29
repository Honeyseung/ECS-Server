package interfaces;

import java.util.Optional;

public interface Data extends Message {
  Optional<String> deviceId();

  Optional<String> sensorNodeId();
}
