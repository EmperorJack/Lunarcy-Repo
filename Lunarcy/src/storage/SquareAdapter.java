package storage;

import game.Square;

import java.lang.reflect.Type;

import com.google.gson.*;

public class SquareAdapter  implements JsonSerializer<Square>, JsonDeserializer<Square> {
	  @Override
	  public JsonElement serialize(Square src, Type typeOfSrc, JsonSerializationContext context) {
	      JsonObject result = new JsonObject();
	      result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
	      result.add("properties", context.serialize(src, src.getClass()));
	      return result;
	  }


	  @Override
	  public Square deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	        throws JsonParseException {
	    JsonObject jsonObject = json.getAsJsonObject();
	    String type = jsonObject.get("type").getAsString();
	    JsonElement element = jsonObject.get("properties");

	    try {
	        String thepackage = "game.";
	        return context.deserialize(element, Class.forName(thepackage + type));
	    } catch (ClassNotFoundException cnfe) {
	        throw new JsonParseException("Unknown element type: " + type, cnfe);
	    }
	  }
	}