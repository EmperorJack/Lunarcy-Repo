package game;

/**
 * This item is used as protection from the bots
 * in the game. When in your inventory bots can not kill you
 *
 * @author evansben1
 *
 */
public class CloakingGadget implements Item {
	private static final long serialVersionUID = -1468450603052040243L;

	private int entityID;

	public CloakingGadget(int entityID) {
		this.entityID = entityID;
	}

	public String getImageName() {
		return "cloak";
	}

	public String getName() {
		return "Cloak #" +entityID;
	}

	public String getDescription() {
		return "When you have a cloaking gadget in your inventory, rovers can not track you. They can however still kill you if they happen to run into you";
	}

	@Override
	public int getEntityID() {
		return entityID;
	}
}
