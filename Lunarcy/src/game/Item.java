package game;

import java.io.Serializable;

/**
 * An item is any in game item that can be picked up
 *
 * @author Robbie
 *
 */

public interface Item extends Entity , Serializable{
	/**
	 * Get the name of the image that represents this in game, without an extension
	 */
	public abstract String getImageName();
	/**
	 * Get the human readable name of the item
	 */
	public abstract String getName();
	/**
	 * Get a brief description of the item
	 */
	public abstract String getDescription();
}
