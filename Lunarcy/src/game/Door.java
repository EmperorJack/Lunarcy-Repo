package game;

public class Door implements Wall {
	private final Key key;
	public Door(Key key){
		this.key = key;
	}
	@Override
	public boolean enter(Player player) {
		return player.getInventory().contains(key);
	}
}
