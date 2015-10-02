package game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Container which contains items.
 * Items can be added or taken out, subclasses will implement access control.
 * @author Robbie
 *
 */
public abstract class Container extends Entity {
	Set<Item> items;
	
	public Container(int entityID, String imageName) {
		super(entityID, imageName);
		items = new HashSet<Item>();
	}
	
	public void addItem(Item item){
		items.add(item);
	}
	
	public boolean hasItem(int itemID){
		for(Item item: items){
			if(item.entityID==itemID){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds the item with specified ID and removes it from the container
	 * @param itemID The ID number of the wanted item
	 * @return The first Item in the set with a matching ID number
	 * @throws IllegalArgumentException if itemID was not found
	 */
	public Item takeItem(Player player, int itemID){
		Item item = null;
		for(Item i : items){
			if(i.entityID == itemID){
				item = i;
			}
		}
		if(item==null){
			throw new IllegalArgumentException("'itemID' does not correspond with an item in this container");
		}
		if(canAccess(player)){
			items.remove(item);
			return item;
		}else{
			return null;
		}
	}
	
	/**
	 * Checks if a player is allowed to access the Container
	 * @param player 
	 * @return True if player can access, False otherwise
	 */
	protected abstract boolean canAccess(Player player);
	
	/**
	 * Returns the set of all items inside this container.
	 * Note: Modifying the returned set will not change the set inside the container,
	 * @return
	 */
	public Set<Item> getItems(){
		return new HashSet<Item>(items);
	}
}
