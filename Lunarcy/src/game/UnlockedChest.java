package game;

public class UnlockedChest extends Container {
	public UnlockedChest(int entityID) {
		super(entityID,"unlockedChest");
	}
	@Override
	protected boolean canAccess(Player player) {
		return true;
	}

}
