package storage;

import game.Wall;

import java.lang.reflect.Type;

import com.google.gson.*;

public class WallAdapter  implements JsonSerializer<Wall>, JsonDeserializer<Wall> {
	  @Override
	  public JsonElement serialize(Wall src, Type typeOfSrc, JsonSerializationContext context) {
	      JsonObject result = new JsonObject();
	      result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
	      result.add("properties", context.serialize(src, src.getClass()));
	      return result;
	  }


	  @Override
	  public Wall deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
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