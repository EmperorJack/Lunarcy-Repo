package game;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Container which contains items.
 * Items can be added or taken out, subclasses will implement access control.
 * @author Robbie
 *
 */
public abstract class Container implements Entity {
	private static final long serialVersionUID = -7267673923708405364L;

	private final int entityID;
	private final int maxSize;
	
	List<Item> items;

	public Container(int entityID, int maxSize) {
		this.entityID = entityID;
		this.maxSize = maxSize;
		items = new ArrayList<Item>();
	}

	public boolean addItem(Item item){
		System.out.println("Container has " + items.size() +" items out of "+maxSize);
		if(item.getEntityID()==this.entityID || items.size() >= maxSize){
			//Cannot put a container inside itself!
			return false;
		}
		return items.add(item);
	}

	/**
	 * Checks if a container has an item that matches the specified ID
	 * @param itemID
	 */
	public boolean hasItem(int itemID){
		for(Item item: items){
			if(item.getEntityID()==itemID){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasSpace(){
		return items.size() < maxSize;
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
			if(i.getEntityID() == itemID){
				item = i;
			}
		}
		if(item!=null){
			items.remove(item);
		}
		return item;
	}

	/**
	 * Checks if a player is allowed to access the Container
	 * @param player
	 * @return True if player can access, False otherwise
	 */
	public abstract boolean canAccess(Player player);

	public int getEntityID(){
		return entityID;
	}
	/**
	 * Returns the set of all items inside this container.
	 * Note: Modifying the returned set will not change the set inside the container,
	 * @return
	 */
	public List<Item> getItems(){
		return new ArrayList<Item>(items);
	}
}
