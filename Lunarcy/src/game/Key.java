package game;

/**
 * This item is used to open doors in the game, has a code that matches a door code.
 * @author Robbie
 *
 */
public class Key implements Item {
	private static final long serialVersionUID = -143449561013171486L;

	private final int entityID;
	public final int keyCode;
	public Key(int entityID, int keyCode){
		this.entityID = entityID;
		this.keyCode = keyCode;
	}

	public int getEntityID() {
		return entityID;
	}

	public String getImageName() {
		return "key";
	}

	public String getName() {
		return "Key "+keyCode;
	}

	public String getDescription() {
		return "Used to access locked doors and chests";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + keyCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (keyCode == other.keyCode)
			return true;
		return false;
	}

	public int getAccessLevel() {
		return keyCode;
	}
}
