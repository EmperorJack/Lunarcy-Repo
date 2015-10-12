package game;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Container which contains items.
 * Items can be added or taken out, subclasses will implement access control.
 * @author Robbie
 *
 */
public abstract class Container extends Entity {
	private static final long serialVersionUID = -7267673923708405364L;

	List<Item> items;

	public Container(int entityID) {
		super(entityID);
		items = new ArrayList<Item>();
	}

	public boolean addItem(Item item){
		return items.add(item);
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
	public Item takeItem(int itemID){
		Item item = null;
		for(Item i : items){
			if(i.entityID == itemID){
				item = i;
			}
		}
		if(item==null){
			throw new IllegalArgumentException("'itemID' does not correspond with an item in this container");
		}
		items.remove(item);
		return item;
	}

	/**
	 * Checks if a player is allowed to access the Container
	 * @param player
	 * @return True if player can access, False otherwise
	 */
	public abstract boolean canAccess(Player player);

	public abstract int getAccessLevel();

	/**
	 * Returns the set of all items inside this container.
	 * Note: Modifying the returned set will not change the set inside the container,
	 * @return
	 */
	public List<Item> getItems(){
		return new ArrayList<Item>(items);
	}
}
