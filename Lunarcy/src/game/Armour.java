package game;

/**
 * This item is used as protection from the bots
 * in the game. When in your inventory bots can not kill you
 *
 * @author evansben1
 *
 */
public class Armour extends Item {

	public Armour(int entityID) {
		super(entityID);
	}

	public String getImageName() {
		return "armour";
	}

	public String getName() {
		return "Armour #" +entityID;
	}

	public String getDescription() {
		return "When you have armout in your inventory, rovers can not kill you";
	}
}
