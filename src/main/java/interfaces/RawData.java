package interfaces;

import java.util.Optional;

public interface RawData extends Data {
  Optional<Object> rawData();
}
