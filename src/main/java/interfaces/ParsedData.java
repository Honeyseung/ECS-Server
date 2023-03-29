package interfaces;

import com.google.gson.JsonObject;

import java.util.Optional;

public interface ParsedData extends Data {
  Optional<JsonObject> data();
}
