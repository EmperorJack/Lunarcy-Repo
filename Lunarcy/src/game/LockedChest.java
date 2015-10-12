package game;

public class LockedChest extends Container {
	private static final long serialVersionUID = -7529638938409484797L;

	private final Key key;
	public LockedChest(int entityID, Key key) {
		super(entityID);
		this.key = key;
	}
	@Override
	public boolean canAccess(Player player) {
		return player.getInventory().contains(key);
	}
	@Override
	public String getImageName() {
		return "lockedChest";
	}

	@Override
	public String getName() {
		return "Locked Chest";
	}

}
