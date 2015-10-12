package game;

/**
 * An item is any in game item that can be picked up
 *
 * @author Robbie
 *
 */
public interface Item extends Entity {
	public abstract String getImageName();
	public abstract String getName();
	public abstract String getDescription();
}
