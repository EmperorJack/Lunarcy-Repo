package game;

public class LockedChest extends Container implements Furniture {
	private static final long serialVersionUID = -7529638938409484797L;

	private final int keyID;
	public LockedChest(int entityID, int keyID) {
		super(entityID);
		if(keyID < 1){
			keyID = 1;
		}else if(keyID > 3){
			keyID = 3;
		}
		this.keyID = keyID;
	}
	@Override
	public boolean canAccess(Player player) {
		return player.hasKey(keyID);
	}
	@Override
	public String getImageName() {
		return "lockedChest" + (isOpen() ? "_open" : "");
	}
	@Override
	public int getAccessLevel() {
		return keyID;
	}
}
