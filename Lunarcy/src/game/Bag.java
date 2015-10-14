package game;

public class Bag extends Container implements Item {
	private static final long serialVersionUID = -3214772972114699038L;

	public Bag(int entityID) {
		super(entityID, 3);
	}

	public Bag() {
		super(0, 3);
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
