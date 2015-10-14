package ui.ApplicationWindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import network.Server;
import storage.Storage;

/**
 * A GUI window for starting a new server.
 *
 * @author evansben1
 *
 */
public class ServerMain extends JFrame {

	private static final long serialVersionUID = 1L;

	// The underlying server
	private Server server;

	// Sliders for choosing
	private JSlider refreshRate;
	private JSlider playerNum;

	// Load buttons
	private JButton loadGame;
	private JButton loadMap;

	// Start/Stop buttons
	private JButton startGame;
	private JButton stopGame;

	// The default map, can be changed by pressing load button
	private String selectedMap = "assets/maps/jacksmap.json";

	public ServerMain() {
		super("Start Game");

		setLayout(new GridBagLayout());

		setPreferredSize(new Dimension(300, 620));

		// Display a message at the top
		addTitle();

		// Add a slider for refresh rate
		addRefreshRateChooser();

		// Add a slider for number of players
		addPlayerNumChooser();

		// Add buttons to start/stop the server
		addStartStopButtons();

		// Add buttons for saving/loading the whole game state
		addSaveLoadButtons();

		// Add a label for the servers IP Adress
		addServerIP();

		// Add a text are for printing output etc
		addConsole();

		pack();

		// When closed we want to actually exit (so it kills the server)
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2,
				(size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);
		setResizable(false);

	}

	/**
	 * Adds a Welcome message at the top of the window
	 */
	private void addTitle() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel title = new JLabel("Lunarcy: Server");
		// Make the font larger (25px)
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));

		// Left/top/bottom padding to center text
		c.insets = new Insets(20, 20, 20, 0);

		// Title label at 0,0 taking up two cells
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		add(title, c);
	}

	/**
	 * Adds a slider for choosing the refresh rate
	 */
	private void addRefreshRateChooser() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel("Refresh rate (millis):");

		// Label is at 0,1 with a width of 2
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		add(label, c);

		refreshRate = new JSlider(0, 250);
		refreshRate.setMajorTickSpacing(50);
		refreshRate.setMinorTickSpacing(10);
		refreshRate.setPaintTicks(true);
		refreshRate.setPaintLabels(true);

		// Slider is at 0,2 with a width of 2
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 15, 0);

		add(refreshRate, c);
	}

	private void addPlayerNumChooser() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel("Num Players:");

		// Label is at 0,3 with a width of 2
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		add(label, c);

		playerNum = new JSlider(1, 5);
		playerNum.setMajorTickSpacing(1);
		playerNum.setPaintTicks(true);
		playerNum.setPaintLabels(true);

		// Slider is at 0,4 with a width of 2
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 20, 0);

		add(playerNum, c);
	}

	/**
	 * Add the start/stop buttons
	 */
	private void addStartStopButtons() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		startGame = new JButton("Start");
		stopGame = new JButton("Stop");

		// When clicked, make start unclickable and stop clickable
		startGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startGame.setEnabled(false);
				stopGame.setEnabled(true);

				loadGame.setEnabled(false);
				loadMap.setEnabled(false);

				// Makes a new server
				try {
					server = new Server(playerNum.getValue(), refreshRate
							.getValue(), selectedMap);
				}
				//if something went wrong when making the server
				catch (IOException exception) {
					//Display a message
					JOptionPane.showMessageDialog(null, "Port Already In Use");

					//And close the program
					System.exit(1);
				}
				server.start();
			}
		});

		// Start button is at 0, 5
		c.gridx = 0;
		c.gridy = 5;
		add(startGame, c);

		// When clicked, make stop unclickable and start clicabe
		stopGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// No game running so cant stop again
				stopGame.setEnabled(false);

				// All other buttons now visible
				startGame.setEnabled(true);
				loadGame.setEnabled(true);
				loadMap.setEnabled(true);

				// Ask if you want to save the server
				int save = JOptionPane.showConfirmDialog(ServerMain.this,
						"Do you want to save?", "Save",
						JOptionPane.YES_NO_OPTION);

				// If they chose yes, then save the gamestate
				if (save == JOptionPane.YES_OPTION) {

					// Retrieve the file to save to
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File(System
							.getProperty("user.dir") + "/savedgames"));
					chooser.showSaveDialog(null);

					// if they chose a file
					if (chooser.getSelectedFile() != null) {
						String filename = chooser.getSelectedFile()
								.getAbsolutePath();

						// Tell server to save the game
						server.saveGamestate(filename);
					}
				}

				// Stop the game
				server.stopServer();

			}
		});

		// Not enabled until start has been clicked
		stopGame.setEnabled(false);

		// Stop button is at 1, 5
		c.gridx = 1;
		c.gridy = 5;

		add(stopGame, c);
	}

	/**
	 * Add buttons for saving/loading
	 **/

	private void addSaveLoadButtons() {
		GridBagConstraints c = new GridBagConstraints();

		loadMap = new JButton("Load Map");

		// When pushed start at a JFileChooser
		loadMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System
						.getProperty("user.dir") + "/assets/maps"));

				// Only show .XML files, as this is our map type
				FileNameExtensionFilter jsonfilter = new FileNameExtensionFilter(
						"json files (*.json)", "json");
				chooser.setFileFilter(jsonfilter);

				// Display the chooser
				chooser.showOpenDialog(null);

				// If they chose an item, set it
				if (chooser.getSelectedFile() != null) {
					selectedMap = chooser.getSelectedFile().getAbsolutePath();
				}
			}

		});

		// Load Map button is at 0,6
		c.gridx = 0;
		c.gridy = 6;
		add(loadMap, c);

		loadGame = new JButton("Load Game");

		loadGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Can not start a game if you have just loaded one
				startGame.setEnabled(false);

				// Can not reload
				loadGame.setEnabled(false);

				// Can not choose another map
				loadMap.setEnabled(false);

				// Can only stop
				stopGame.setEnabled(true);

				// Retrieve the file to load
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System
						.getProperty("user.dir") + "/savedgames"));
				chooser.showOpenDialog(null);

				// Don't do anything if they cancel the chooser
				if (chooser.getSelectedFile() == null) {
					return;
				}

				String filename = chooser.getSelectedFile().getAbsolutePath();

				// Make a new server with the specified info
				try {
					server = new Server(refreshRate.getValue(), Storage
							.loadState(filename));
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Port Already In Use");
					System.exit(1);
				}
				server.start();
			}

		});

		// Load Game button is at 1,6
		c.gridx = 1;
		c.gridy = 6;
		add(loadGame, c);

	}

	/**
	 * Adds a label for displaying the servers IP
	 */
	private void addServerIP() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel ipLabel;

		// Get the IP Adress, can throw an Unknown Host expection so scheck for
		// this
		try {
			ipLabel = new JLabel("IP Address: "
					+ InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			ipLabel = new JLabel("IP Address: ERROR UNKNOWN HOST");
		}

		// IP address is at 0,7
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.insets = new Insets(10, 0, 0, 0);

		add(ipLabel, c);

	}

	/**
	 * Adds a print out area for the servers output
	 */
	private void addConsole() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JTextArea console = new JTextArea();

		// Configure the text area to get the input from stdout
		PrintStream printStream = new PrintStream(new ConsoleOutput(console));
		System.setOut(printStream);

		// Not directly editable by user
		console.setEditable(false);
		console.setPreferredSize(new Dimension(getWidth(), 250));

		// Console is at 0, 8
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.insets = new Insets(15, 0, 10, 0);

		add(console, c);
	}

	/**
	 * Used for remapping stdout to the textarea to display any messages. I
	 * received the base idea for this from: http://www.codejava.net/
	 *
	 * @author b
	 *
	 */
	private class ConsoleOutput extends OutputStream {

		JTextArea console;

		public ConsoleOutput(JTextArea console) {
			this.console = console;
		}

		@Override
		public void write(int i) throws IOException {
			// Clear the textarea if the text gets too long
			if (console.getLineCount() > 15) {
				console.setText("");
			}

			console.append(Character.toString((char) i));
		}

	}

	public static void main(String[] args) {
		new ServerMain();
	}

}
