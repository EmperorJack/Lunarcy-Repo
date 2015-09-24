package game;

/**
 * This item is used to open doors in the game, has a code that matches a door code. 
 * @author Robbie
 *
 */
public class Key implements Item {
	private int keyCode;
	public Key(int keyCode){
		this.keyCode = keyCode;
	}
}
