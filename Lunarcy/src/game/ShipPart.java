package game;

public class ShipPart implements Item {
	private static final long serialVersionUID = 1206556005810938273L;

	private final int entityID;
	private PartType type;
	public ShipPart(int entityID, int partID) {
		this.entityID = entityID;
		if(partID < 0 || partID > PartType.values().length){
			partID = 0;
		}
		type = PartType.values()[partID];
	}

	public int getEntityID() {
		return entityID;
	}

	public int getTypeID(){
		return type.ordinal();
	}


	public String getImageName() {
		return "shipPart_"+type;
	}

	public String getName(){
		return type.toString().replace('_', ' ');
	}

	public String getDescription(){
		return "Use this to fix the broken escape pod!";
	}

	enum PartType{
		ENGINE, FUEL_CORE, GRAVITY_CORE, GPS, WARP_DRIVE
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ShipPart other = (ShipPart) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
