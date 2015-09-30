package game;

import java.io.Serializable;

public abstract class Entity implements Serializable{
	public final int entityID;
	public Entity(int entityID){
		this.entityID = entityID;
	}
}
