package game;

public class ShipPart extends Item {
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
}
