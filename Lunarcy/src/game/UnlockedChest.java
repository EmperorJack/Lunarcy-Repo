package game;

public class UnlockedChest extends Container {
	private static final long serialVersionUID = -1401298737961190449L;

	public UnlockedChest(int entityID) {
		super(entityID);
	}
	@Override
	protected boolean canAccess(Player player) {
		return true;
	}
	@Override
	public String getImageName() {
		return "unlockedChest";
	}

}
