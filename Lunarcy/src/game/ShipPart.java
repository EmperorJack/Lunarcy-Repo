package game;

public class ShipPart extends Item {
	private static final long serialVersionUID = 1206556005810938273L;

	private PartType type;
	public ShipPart(int entityID, int partID) {
		super(entityID);
		if(partID < 0 || partID > PartType.values().length){
			partID = 0;
		}
		type = PartType.values()[partID];
	}

	public int getTypeID(){
		return type.ordinal();
	}

	public String getImageName() {
		return "shipPart_"+type;
	}

	public String getName(){
		return type + " #" + entityID;
	}

	public String getDescription(){
		return "Use this to fix the broken escape pod!";
	}

	enum PartType{
		ENGINE, FUEL_CORE, COOLANT, GPS, WARP_DRIVE
	}

	@Override
	public int hashCode() {
		return 31 * ((type == null) ? 0 : type.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if(obj==null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShipPart other = (ShipPart) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
