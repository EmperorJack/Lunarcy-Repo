package game;

import java.io.Serializable;

/**
 * This item is used as protection from the bots
 * in the game. When in your inventory bots can not kill you
 *
 * @author evansben1
 *
 */
public class Armour implements Item, Serializable {
	private static final long serialVersionUID = 8530995197024927216L;

	private final int entityID;

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
