package game;

public class ShipPart extends Item {
	private PartType type;
	public ShipPart(int entityID, int partID) {
		super(entityID,"shipPart");
		if(partID < 0 || partID > PartType.values().length){
			partID = 0;
		}
		type = PartType.values()[partID];
	}
	
	public int getTypeID(){
		return type.ordinal();
	}
	
	enum PartType{
		engine
	}
}
