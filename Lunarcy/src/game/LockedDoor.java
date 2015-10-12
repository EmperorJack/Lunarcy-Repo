package game;

public class LockedDoor implements Wall {
	private static final long serialVersionUID = 9109599272298663549L;

	private final int keyCode;
	public LockedDoor(int keyCode){
		this.keyCode = keyCode;
	}
	@Override
	public boolean canPass(Character character) {
		if(character == null || !(character instanceof Player)){
			return false;
		}
		Player player = (Player)character;
		return player.hasKey(keyCode);
	}
}
