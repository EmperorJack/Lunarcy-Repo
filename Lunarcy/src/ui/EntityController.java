package ui;

public class EntityController {

	private Inventory inventory;
	private EntityView entityView;

	public EntityController() {

	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

}
