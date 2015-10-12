package game;

public class Chest extends Container {
	private static final long serialVersionUID = -1401298737961190449L;

	public Chest(int entityID) {
		super(entityID);
	}
	@Override
	public boolean canAccess(Player player) {
		return true;
	}
	@Override
	public String getImageName() {
		return "unlockedChest";
	}
	@Override
	public String getName() {
		return "Chest";
	}

}
