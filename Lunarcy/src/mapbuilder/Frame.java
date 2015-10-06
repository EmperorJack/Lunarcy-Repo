package mapbuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
 * Swing window for the map builder. Based off the window for the main game application.
 * @author Kelly
 *
 */
@SuppressWarnings("serial")
public class Frame extends JFrame implements ActionListener{
	Canvas canvas;
	MapBuilder mapBuilder;

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
		JMenu optionMenu, submenu, toggleMenu;
		menuBar = new JMenuBar();
		optionMenu = new JMenu("Options");
		toggleMenu = new JMenu("Toggles");
		menuBar.add(optionMenu);
		menuBar.add(toggleMenu);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem loadMenuItem = new JMenuItem("Load");
		JCheckBoxMenuItem insideToggle = new JCheckBoxMenuItem("Inside Tiles");
		JCheckBoxMenuItem wallToggle = new JCheckBoxMenuItem("Walls");
		optionMenu.add(saveMenuItem);
		optionMenu.add(loadMenuItem);
		toggleMenu.add(insideToggle);
		toggleMenu.add(wallToggle);
		saveMenuItem.addActionListener(this);
		loadMenuItem.addActionListener(this);

		insideToggle.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					mapBuilder.insideTilesOn();
				} else {
					mapBuilder.insideTilesOff();
				}

			}
		});

		wallToggle.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
					mapBuilder.toggleWalls();
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

	public static void main(String[] args) {
		new Frame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Save")){
			mapBuilder.save();
		}
		if(e.getActionCommand().equals("Load")){
			mapBuilder.load();
		}
	}
}