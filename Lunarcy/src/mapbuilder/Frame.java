package mapbuilder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Swing window for the map builder. Based off the window for the main game
 * application.
 *
 * @author Kelly
 *
 */
@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener {
	Canvas canvas;
	MapBuilder mapBuilder;


	final JCheckBoxMenuItem insideToggle = new JCheckBoxMenuItem("Inside Tiles");
	final JCheckBoxMenuItem wallToggle = new JCheckBoxMenuItem("Walls");
	final JCheckBoxMenuItem doorToggle = new JCheckBoxMenuItem("Doors");
	final JCheckBoxMenuItem greenDoorToggle = new JCheckBoxMenuItem("Green Doors");
	final JCheckBoxMenuItem orangeDoorToggle = new JCheckBoxMenuItem("Orange Doors");
	final JCheckBoxMenuItem redDoorToggle = new JCheckBoxMenuItem("Red Doors");
	final JCheckBoxMenuItem containerToggle = new JCheckBoxMenuItem("Containers");
	final JCheckBoxMenuItem greenContainerToggle = new JCheckBoxMenuItem("Green Containers");
	final JCheckBoxMenuItem orangeContainerToggle = new JCheckBoxMenuItem("Orange Containers");
	final JCheckBoxMenuItem redContainerToggle = new JCheckBoxMenuItem("Red Containers");
	final JCheckBoxMenuItem furnitureToggle = new JCheckBoxMenuItem("Furniture");
	final JCheckBoxMenuItem removeFurniture = new JCheckBoxMenuItem("Remove Furniture");

	public Frame() {
		setTitle("Cool Map Builder");
		setSize(1280, 755);

		// Catch exit with confirmation dialog (ripped from game's frame)
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(null, new JLabel(
						"Are you sure you want to exit Cool Map Builder?"),
						"Exit Game", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				// Only close if they confirm they want to close
				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		mapBuilder = new MapBuilder();

		JMenuBar menuBar;
		JMenu optionMenu, toolMenu, toggleMenu;
		menuBar = new JMenuBar();
		optionMenu = new JMenu("Options");
		toggleMenu = new JMenu("Toggles");
		toolMenu = new JMenu("Tools");
		menuBar.add(optionMenu);
		menuBar.add(toggleMenu);
		menuBar.add(toolMenu);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem loadMenuItem = new JMenuItem("Load");
		JMenuItem setWalkable = new JMenuItem("Set Walkable");
		JMenuItem setBlank = new JMenuItem("Set Blank");
		JMenuItem setShip = new JMenuItem("Set Ship");
		JMenuItem addPlayerSpawn = new JMenuItem("Add Player Spawn Point");
		JMenuItem removePlayerSpawn = new JMenuItem("Remove Player Spawn Point");
		JMenuItem addRoverSpawn = new JMenuItem("Add Rover Spawn Point");
		JMenuItem removeRoverSpawn = new JMenuItem("Remove Rover Spawn Point");
		JMenuItem intialiseItems = new JMenuItem("Initialise Items");
		optionMenu.add(saveMenuItem);
		optionMenu.add(loadMenuItem);
		toggleMenu.add(insideToggle);
		toggleMenu.add(wallToggle);
		toggleMenu.add(doorToggle);
		toggleMenu.add(greenDoorToggle);
		toggleMenu.add(orangeDoorToggle);
		toggleMenu.add(redDoorToggle);
		toggleMenu.add(containerToggle);
		toggleMenu.add(greenContainerToggle);
		toggleMenu.add(orangeContainerToggle);
		toggleMenu.add(redContainerToggle);
		toggleMenu.add(furnitureToggle);
		toggleMenu.add(removeFurniture);
		toolMenu.add(setWalkable);
		toolMenu.add(setBlank);
		toolMenu.add(setShip);
		toolMenu.add(addPlayerSpawn);
		toolMenu.add(removePlayerSpawn);
		toolMenu.add(addRoverSpawn);
		toolMenu.add(removeRoverSpawn);
		toolMenu.add(intialiseItems);
		saveMenuItem.addActionListener(this);
		loadMenuItem.addActionListener(this);
		setWalkable.addActionListener(this);
		setBlank.addActionListener(this);
		setShip.addActionListener(this);
		addPlayerSpawn.addActionListener(this);
		removePlayerSpawn.addActionListener(this);
		addRoverSpawn.addActionListener(this);
		removeRoverSpawn.addActionListener(this);
		intialiseItems.addActionListener(this);

		insideToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(insideToggle);
					mapBuilder.insideTilesOn();
				} else {
					mapBuilder.insideTilesOff();
				}

			}
		});

		wallToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(wallToggle);
					mapBuilder.wallsOn();
				} else {
					mapBuilder.wallsOff();
				}
			}
		});

		doorToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(doorToggle);
					mapBuilder.doorsOn(0);
				} else {
					mapBuilder.doorsOff();
				}
			}
		});

		greenDoorToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(greenDoorToggle);
					mapBuilder.doorsOn(1);
				} else {
					mapBuilder.doorsOff();
				}
			}
		});

		orangeDoorToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(orangeDoorToggle);
					mapBuilder.doorsOn(2);
				} else {
					mapBuilder.doorsOff();
				}
			}
		});


		redDoorToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(redDoorToggle);
					mapBuilder.doorsOn(3);
				} else {
					mapBuilder.doorsOff();
				}
			}
		});


		containerToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(containerToggle);
					mapBuilder.containersOn(0);
				} else {
					mapBuilder.containersOff();
				}
			}
		});

		greenContainerToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(greenContainerToggle);
					mapBuilder.containersOn(1);
				} else {
					mapBuilder.containersOff();
				}
			}
		});

		orangeContainerToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(orangeContainerToggle);
					mapBuilder.containersOn(2);
				} else {
					mapBuilder.containersOff();
				}
			}
		});

		redContainerToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(orangeContainerToggle);
					mapBuilder.containersOn(3);
				} else {
					mapBuilder.containersOff();
				}
			}
		});

		furnitureToggle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(furnitureToggle);
					mapBuilder.furnitureOn();
				} else {
					mapBuilder.furnitureOff();
				}
			}
		});


		removeFurniture.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					setTogglesFalse(removeFurniture);
					mapBuilder.removeFurnitureOn();
				} else {
					mapBuilder.removeFurnitureOff();
				}
			}
		});



		final JPanel panel = new JPanel(new BorderLayout());
		canvas = new Canvas(mapBuilder);
		canvas.setFocusable(true);
		panel.add(canvas, BorderLayout.CENTER);
		setJMenuBar(menuBar);
		add(panel);

		setResizable(false);
		setVisible(true);

	}

	public void setTogglesFalse(JCheckBoxMenuItem checkBoxFrom){
		if (!checkBoxFrom.equals(wallToggle)) wallToggle.setState(false);
		if (!checkBoxFrom.equals(doorToggle)) doorToggle.setState(false);
		if (!checkBoxFrom.equals(greenDoorToggle)) greenDoorToggle.setState(false);
		if (!checkBoxFrom.equals(orangeDoorToggle)) orangeDoorToggle.setState(false);
		if (!checkBoxFrom.equals(redDoorToggle)) redDoorToggle.setState(false);
		if (!checkBoxFrom.equals(containerToggle)) containerToggle.setState(false);
		if (!checkBoxFrom.equals(greenContainerToggle)) greenContainerToggle.setState(false);
		if (!checkBoxFrom.equals(orangeContainerToggle)) orangeContainerToggle.setState(false);
		if (!checkBoxFrom.equals(redContainerToggle)) redContainerToggle.setState(false);
		if (!checkBoxFrom.equals(furnitureToggle))  furnitureToggle.setState(false);
		if (!checkBoxFrom.equals(removeFurniture)) removeFurniture.setState(false);
	}

	public static void main(String[] args) {
		new Frame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Save")) {
			mapBuilder.save();
		}
		if (e.getActionCommand().equals("Load")) {
			mapBuilder.load();
		}
		if (e.getActionCommand().equals("Set Walkable")) {
			mapBuilder.setWalkable();
		}
		if (e.getActionCommand().equals("Set Blank")) {
			mapBuilder.setBlank();
		}
		if (e.getActionCommand().equals("Set Ship")) {
			mapBuilder.setShip();
		}
		if (e.getActionCommand().equals("Add Player Spawn Point")) {
			mapBuilder.addPlayerSpawnPoint();
		}
		if (e.getActionCommand().equals("Remove Player Spawn Point")) {
			mapBuilder.removePlayerSpawnPoint();
		}
		if (e.getActionCommand().equals("Add Rover Spawn Point")) {
			mapBuilder.addRoverSpawnPoint();
		}
		if (e.getActionCommand().equals("Remove Rover Spawn Point")) {
			mapBuilder.removeRoverSpawnPoint();
		}
		if (e.getActionCommand().equals("Initialise Items")) {
			mapBuilder.initialiseItems();
		}
	}
}
