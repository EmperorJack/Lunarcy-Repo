package game;

/**
 * This item is used as protection from the bots
 * in the game. When in your inventory bots can not kill you
 *
 * @author evansben1
 *
 */
public class Armour implements Item {
	private int entityID;

	public Armour(int entityID) {
		this.entityID = entityID;
	}


	public String getImageName() {
		return "armour";
	}

	public String getName() {
		return "Armour #" +entityID;
	}

	public String getDescription() {
		return "When you have armour in your inventory, rovers can not kill you";
	}


	@Override
	public int getEntityID() {
		return entityID;
	}
}
