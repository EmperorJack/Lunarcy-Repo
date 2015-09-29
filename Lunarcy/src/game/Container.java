package game;

import java.util.HashSet;
import java.util.Set;

public abstract class Container extends Entity {
	Set<Item> items;
	public Container(int itemID) {
		super(itemID);
		items = new HashSet<Item>();
	}
}
