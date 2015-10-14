package storage;

import bots.MoveStrategy;

import java.lang.reflect.Type;

import com.google.gson.*;

public class MoveStrategyAdapter  implements JsonSerializer<MoveStrategy>, JsonDeserializer<MoveStrategy> {
	  @Override
	  public JsonElement serialize(MoveStrategy src, Type typeOfSrc, JsonSerializationContext context) {
	      JsonObject result = new JsonObject();
	      result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
	      result.add("properties", context.serialize(src, src.getClass()));
	      return result;
	  }


	  @Override
	  public MoveStrategy deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	        throws JsonParseException {
	    JsonObject jsonObject = json.getAsJsonObject();
	    String type = jsonObject.get("type").getAsString();
	    JsonElement element = jsonObject.get("properties");

	    try {
	        String thepackage = "bots.";
	        return context.deserialize(element, Class.forName(thepackage + type));
	    } catch (ClassNotFoundException cnfe) {
	        throw new JsonParseException("Unknown element type: " + type, cnfe);
	    }
	  }
	}