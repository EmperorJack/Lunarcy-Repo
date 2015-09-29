package game;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Container which contains items.
 * Items can be added or taken out.
 * @author Robbie
 *
 */
public abstract class Container extends Entity {
	Set<Item> items;
	public Container(int itemID) {
		super(itemID);
		items = new HashSet<Item>();
	}
	
	public void addItem(Item item){
		items.add(item);
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
			if(i.itemID == itemID){
				item = i;
			}
		}
		if(item==null){
			throw new IllegalArgumentException("'itemID' does not correspond with an item in this container");
		}
		items.remove(item);
		return item;
	}
}
