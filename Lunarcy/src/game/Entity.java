package game;

import java.io.Serializable;

public abstract class Entity implements Serializable{
	public final String imageName;
	public final int entityID;
	public Entity(int entityID, String imageName){
		this.entityID = entityID;
		this.imageName = imageName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + entityID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (entityID != other.entityID)
			return false;
		return true;
	}


}
