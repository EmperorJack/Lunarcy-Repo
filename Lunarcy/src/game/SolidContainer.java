package game;

public abstract class SolidContainer extends Container implements Furniture {
	private static final long serialVersionUID = -568751828919040432L;

	private boolean isOpen;

	public SolidContainer(int entityID) {
		super(entityID);
		isOpen = false;
	}

	public boolean isOpen(){
		return isOpen;
	}

	public void open(Player player){
		if(canAccess(player)){
			isOpen = true;
		}
	}

	public void close(){
		isOpen = false;
	}

	public boolean addItem(Item item){
		if(!isOpen()){
			return false;
		}
		return items.add(item);
	}

	public Item takeItem(int itemID){
		if(!isOpen){
			return null;
		}
		Item item = null;
		for(Item i : items){
			if(i.getEntityID() == itemID){
				item = i;
			}
		}
		if(item==null){
			throw new IllegalArgumentException("'itemID' does not correspond with an item in this container");
		}
		items.remove(item);
		return item;
	}

	public abstract int getAccessLevel();
}