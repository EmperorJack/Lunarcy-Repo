package game;

public class UnlockedChest extends Container {
	public UnlockedChest(int entityID) {
		super(entityID);
	}
	@Override
	protected boolean canAccess(Player player) {
		return true;
	}

}
