package game;

/**
 * This item is used as protection from the bots
 * in the game. When in your inventory bots can not kill you
 *
 * @author evansben1
 *
 */
public class CloakingGadget extends Item {

	public CloakingGadget(int entityID) {
		super(entityID);
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
}
