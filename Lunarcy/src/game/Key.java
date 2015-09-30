package game;

/**
 * This item is used to open doors in the game, has a code that matches a door code. 
 * @author Robbie
 *
 */
public class Key extends Item {
	public final int keyCode;
	public Key(int entityID, int keyCode){
		super(entityID);
		this.keyCode = keyCode;
	}
}
