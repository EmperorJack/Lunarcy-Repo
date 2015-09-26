package game;

public class LockedDoor implements Wall {
	private final Key key;
	public LockedDoor(Key key){
		this.key = key;
	}
	@Override
	public boolean enter(Player player) {
		return player.getInventory().contains(key);
	}
}
