package ui;

import game.Bag;
import game.Entity;
import game.GameState;
import game.Item;
import game.Location;
import game.Player;
import game.SolidContainer;
import game.WalkableSquare;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import ui.ApplicationWindow.BagView;
import ui.ApplicationWindow.ContainerView;
import ui.ApplicationWindow.EntityView;
import ui.ApplicationWindow.InventoryView;
import ui.ApplicationWindow.PopupDisplay;
import ui.renderer.Canvas;
import network.Client;
import network.CloseAction;
import network.DropAction;
import network.MoveAction;
import network.OpenAction;
import network.OrientAction;
import network.PickupAction;
import network.PutAction;

/**
 * This class listens for player input to allow interaction between the UI
 * componenets and player movement/actions.
 *
 * @author evansben1 and Jack
 *
 */
public class InteractionController implements KeyListener, MouseListener,
		MouseMotionListener {

	// Drawing components
	private InventoryView inventoryView;
	private EntityView entityView;
	private ContainerView containerView;
	private BagView bagView;
	private PopupDisplay popupDisplay;

	// Dragging/dropping items

	// Dragging out of the inventory
	private Item draggedFromItem;

	// Dragging into inventory
	private Item draggedToItem;

	// The coordinates of the item currently being dragged
	private int draggedX;
	private int draggedY;

	// The coordinates for dragging if none are specified
	private final int DEFAULT_X = -1000;
	private final int DEFAULT_Y = -1000;

	// Client related fields
	private Client client;
	private Player player;

	// Canvas field
	private Canvas canvas;
	private GameState gameState;

	public InteractionController(Client client, GameState gamestate,
			Player player, Canvas canvas) {
		this.client = client;
		this.player = player;
		this.canvas = canvas;
		this.gameState = gamestate;

		// Initialize the values for dragging/dropping items
		resetDragValues();

		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

	}

	/*--------- Action methods --------- */

	private void dropItem(int itemID) {
		System.out.println("Dropping " + itemID);
		client.sendAction(new DropAction(player.getId(), itemID));
	}

	private void putItem(int containerID, int itemID) {
		client.sendAction(new PutAction(player.getId(), containerID, itemID));
	}

	private void pickupItem(int itemID) {
		client.sendAction(new PickupAction(player.getId(), itemID));
	}

	private void openContainer() {
		client.sendAction(new OpenAction(player.getId()));
	}

	private void closeContainer() {
		client.sendAction(new CloseAction(player.getId()));
	}

	/*--------- User input Listening methods ---------*/

	@Override
	public void keyPressed(KeyEvent e) {

		// identify which key pressed
		switch (e.getKeyCode()) {

		// move forward
		case KeyEvent.VK_W:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation()));
			break;

		// strafe left
		case KeyEvent.VK_A:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().left()));
			break;

		// move back
		case KeyEvent.VK_S:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().opposite()));
			break;

		// strafe right
		case KeyEvent.VK_D:
			client.sendAction(new MoveAction(player.getId(), player
					.getOrientation().right()));
			break;

		// turn left
		case KeyEvent.VK_Q:
			client.sendAction(new OrientAction(player.getId(), true));
			break;

		// turn right
		case KeyEvent.VK_E:
			client.sendAction(new OrientAction(player.getId(), false));
			break;

		// Hide/show a popup of current squares info
		case KeyEvent.VK_SPACE:
			// If the menu is open, close it
			if (!popupDisplay.isSet()) {
				Location location = player.getLocation();
				WalkableSquare square = (WalkableSquare) gameState
						.getSquare(location);
				popupDisplay.set(square.getName() + " at " + location,
						square.getDescription());
				return;
			}
			break;
		}

		// Reset our popup
		popupDisplay.set(null, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// adjust the x and y to the canvas scaling
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// If you click the inventory, check if a bag was clicked
		if (inventoryView.onBar(x, y)) {
			Item selectedItem = inventoryView.getItemAt(x, y);

			// If they select a bag
			if (selectedItem instanceof Bag) {
				Bag bag = (Bag) selectedItem;
				bagView.update(bag);
				return;
			}

		}

		// attempt to get a container from entity view
		SolidContainer clickedContainer = entityView.getSolidContainerAt(x, y);

		// If an closed solid container is clicked
		if (clickedContainer != null && !clickedContainer.isOpen()) {
			// Open it
			openContainer();
		}
		// Else close the container
		else if (clickedContainer != null && clickedContainer.isOpen()) {
			closeContainer();
		}

		// On a right click, show item descriptions
		if (SwingUtilities.isRightMouseButton(e)) {

			Item item = entityView.getItemAt(x, y);

			if (item != null) {
				popupDisplay.set(item.getName(), item.getDescription());
				return;
			}

		}
		// Hide the popup
		popupDisplay.set(null, null);

		// Hide the bagview
		bagView.update(null);

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// If the inventory was clicked
		if (inventoryView.onBar(x, y)) {
			// We are dragged an item from the inventory
			draggedFromItem = inventoryView.getItemAt(x, y);
			draggedToItem = null;

			draggedX = x;
			draggedY = y;
			return;
		}

		// If the bag bar was clicked
		if (bagView.onBar(x, y)) {
			//We can drag this item to our inventory
			draggedToItem = bagView.getItemAt(x, y);
			draggedFromItem = null;

			draggedX = x;
			draggedY = y;
			return;

		}

		// If the container menu was clicked
		if (containerView.getContainer() != null && containerView.onBar(x, y)) {
			// Get the item which was clicked
			draggedToItem = containerView.getItemAt(x, y);
			draggedFromItem = null;

			return;
		}

		// If they click an item in the square
		Entity entity = entityView.getItemAt(x, y);
		if (entity != null && entity instanceof Item) {
			// Set the item to be picked up
			draggedToItem = (Item) entity;
			draggedFromItem = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		// Set the coordinates of the current item being dragged
		if (draggedFromItem != null || draggedToItem != null) {
			draggedX = x;
			draggedY = y;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

		int x = (int) (e.getX() / canvas.getScaling());
		int y = (int) (e.getY() / canvas.getScaling());

		SolidContainer container = entityView.getSolidContainerAt(x, y);

		// If they drop into a bag from their inventory
		if (draggedFromItem != null && bagView.onBar(x, y) && bagView.getBag() != null) {
			// Put the item in the bag
			putItem(bagView.getBag().getEntityID(),
					draggedFromItem.getEntityID());
		}
		// If they drag the item to an open container
		else if (draggedFromItem != null && container != null
				&& container.isOpen()) {
			// Put the item in the container
			putItem(container.getEntityID(), draggedFromItem.getEntityID());
		}

		// If the item was not released on the inventory bar, and there was an
		// item currently being dragged, drop it
		else if (draggedFromItem != null && !inventoryView.onBar(x, y)) {

			// Drop the dragged from item
			dropItem(draggedFromItem.getEntityID());
		}

		// If it was released on the inventory bar, check if an item was being
		// dragged
		// from the world in
		else if (inventoryView.onBar(x, y) && draggedToItem != null) {
			// Pickup the item which was dragged onto the inventory
			pickupItem(draggedToItem.getEntityID());
		}

		// Reset all the dragged item settings
		resetDragValues();

		// Close bagview
		bagView.update(null);
	}

	/**
	 * Set all the fields related to dragging back to their default.
	 */
	private void resetDragValues() {
		draggedToItem = null;
		draggedFromItem = null;
		draggedX = DEFAULT_X;
		draggedY = DEFAULT_Y;
	}

	/*---------Getter/Setter methods---------*/

	public void setInventory(InventoryView inventory) {
		this.inventoryView = inventory;
	}

	public void updatePopup(String title, String desc){
		popupDisplay.set(title, desc);
	}

	public void setBagView(BagView bagView) {
		this.bagView = bagView;
	}

	public void setPopup(PopupDisplay popup) {
		this.popupDisplay = popup;
	}

	public void setContainerView(ContainerView containerView) {
		this.containerView = containerView;
	}

	public void setEntityView(EntityView entityView) {
		this.entityView = entityView;
	}

	public void update(Player player, GameState gameState) {
		this.player = player;
		this.gameState = gameState;
	}

	/**
	 * If either the item being dragged from the inventory, or the item being
	 * dragged to the inventory is non null, returns this item (only one can be
	 * non null at a time).
	 *
	 * If both are null, returns null.
	 *
	 * @return
	 */
	public Item getDraggedItem() {

		if (draggedFromItem != null) {
			return draggedFromItem;
		}

		if (draggedToItem != null) {
			return draggedToItem;
		}

		return null;
	}

	public int getDraggedItemX() {
		return draggedX;
	}

	public int getDraggedItemY() {
		return draggedY;
	}

	/*---------Unused listener methods ---------*/

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
