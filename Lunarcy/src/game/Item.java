package game;

/**
 * An item is any in game item that can be picked up
 *
 * @author Robbie
 *
 */
public abstract class Item extends Entity {
	public Item(int entityID) {
		super(entityID);
	}
	public abstract String getName();
	public abstract String getDescription();
}
