package game;

import java.io.Serializable;

public abstract class Entity implements Serializable{
	public final String imageName;
	public final int entityID;
	public Entity(int entityID, String imageName){
		this.entityID = entityID;
		this.imageName = imageName;
	}
}
