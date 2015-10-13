package game;

public class Bag extends Container implements Item {

	public Bag(int entityID) {
		super(entityID);
	}

	public String getImageName() {
		return "bag";
	}

	public String getName() {
		return "Bag";
	}

	public String getDescription() {
		return "A bag that can hold items";
	}

	public boolean canAccess(Player player) {
		return true;
	}
}
