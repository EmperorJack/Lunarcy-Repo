package game;

import java.io.Serializable;

public interface Furniture extends Serializable{
	/**
	 * Get the name of the image that represents this in game, without an extension
	 */
	public abstract String getImageName();
}
