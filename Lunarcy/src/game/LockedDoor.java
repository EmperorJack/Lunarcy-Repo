package game;

public class LockedDoor implements Wall {
	private static final long serialVersionUID = 9109599272298663549L;

	private final Key key;
	public LockedDoor(Key key){
		this.key = key;
	}
	@Override
	public boolean pass(Character character) {
		if(character == null || !(character instanceof Player)){
			return false;
		}
		Player player = (Player)character;
		return player.getInventory().contains(key);
	}
}
