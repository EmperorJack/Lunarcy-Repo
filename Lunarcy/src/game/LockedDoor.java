package game;

public class LockedDoor implements Wall {
	private static final long serialVersionUID = 9109599272298663549L;

	private final Key key;
	public LockedDoor(Key key){
		this.key = key;
	}
	@Override
	public boolean pass(Player player) {
		return player.getInventory().contains(key);
	}
}
