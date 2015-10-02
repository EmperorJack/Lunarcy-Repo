package game;

public class LockedChest extends Container {
	private final Key key;
	public LockedChest(int entityID, Key key) {
		super(entityID,"lockedChest");
		this.key = key;
	}
	@Override
	protected boolean canAccess(Player player) {
		return player.getInventory().contains(key);
	}

}
