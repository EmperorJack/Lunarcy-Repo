package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import control.Server;
import storage.Storage;

/**
 * A GUI window for starting a new server.
 *
 * @author Ben
 *
 */
public class ServerMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private Server server;
	private JSlider refreshRate;
	private JSlider playerNum;
	private JTextArea console;
	private JButton load;

	public ServerMain() {
		super("Start Game");

		setLayout(new GridBagLayout());

		setPreferredSize(new Dimension(320, 600));

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

		//Add a label for the servers IP Adress
		addServerIP();

		// Add a text are for printing output etc
		addConsole();


		pack();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2, (size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);
		setResizable(false);

	}


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

	private void addStartStopButtons() {
		GridBagConstraints c = new GridBagConstraints();

		final JButton start = new JButton("Start");
		final JButton stop = new JButton("Stop");

		// When clicked, make start unclickable and stop clickable
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				stop.setEnabled(true);
				load.setEnabled(false);

				// Makes a new thread, which deals with the server

				server = new Server(playerNum.getValue(), refreshRate.getValue());

				new Thread(new Runnable() {
					public void run() {
						server.run();
					}
				}).start();


			}
		});

		// Start button is at 0, 5
		c.gridx = 0;
		c.gridy = 5;
		add(start, c);

		// When clicked, make stop unclickable and start clicabe
		stop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(true);
				stop.setEnabled(false);
				load.setEnabled(true);

				//Stop the game
				//server.stop();

				//Ask if you want to save the server
				int save = JOptionPane.showConfirmDialog(
					    ServerMain.this,
					    "Do you want to save?",
					    "Save",
					    JOptionPane.YES_NO_OPTION);

				//If they chose yes, then save
				if(save == JOptionPane.YES_OPTION){

					//Retrieve the file to save to
					JFileChooser chooser = new JFileChooser();
					chooser.showSaveDialog(null);
					String filename = chooser.getSelectedFile().getAbsolutePath();

					//Make a new server, using the saved gamestate
					server.saveGamestate(filename);
				}

			}
		});

		// Not enabled until start has been clicked
		stop.setEnabled(false);

		// Stop button is at 1, 5
		c.gridx = 1;
		c.gridy = 5;

		add(stop, c);
	}

	private void addSaveLoadButtons() {
		GridBagConstraints c = new GridBagConstraints();

		load = new JButton("Load");

		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				//Retrieve the file to load
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				String filename = chooser.getSelectedFile().getAbsolutePath();

				//Make a new server with the specified info
				server = new Server(playerNum.getValue(), refreshRate.getValue(), Storage.loadState(filename));

				//Make a new thread, as server.run() is non terminating
				new Thread(new Runnable() {
					public void run() {
						server.run();
					}
				}).start();
			}

		});


		// Load button is at 0,6
		c.gridx = 0;
		c.gridy = 6;
		add(load, c);

	}


	private void addServerIP() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel ipLabel;

		//Get the IP Adress, can throw an Unknown Host expection so scheck for this
		try {
			ipLabel = new JLabel("IP Address: " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			ipLabel = new JLabel("IP Address: ERROR UNKNOWN HOST");
		}

		//IP address is at 0,7
		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 2;
		c.insets = new Insets(10, 0, 0, 0);

		add(ipLabel, c);


	}

	private void addConsole() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		console = new JTextArea();

		//Configure the text area to get the input from stdout
		PrintStream printStream = new PrintStream(new ConsoleOutput(console));
		System.setOut(printStream);


		// Not directly editable by user
		console.setEditable(false);
		console.setPreferredSize(new Dimension(getWidth(), 250));

		// Console is at 0, 8
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;
		c.insets = new Insets(15, 0, 0, 0);

		add(console, c);
	}

	/**
	 * Used for remapping stdout to the textarea
	 * to display any messages.
	 *
	 * @author b
	 *
	 */
	private class ConsoleOutput extends OutputStream {

		JTextArea console;

		public ConsoleOutput(JTextArea console){
			this.console = console;

		}

		@Override
		public void write(int i) throws IOException {
			console.append(Character.toString ((char) i));
			//Scroll down to bottom
			console.setCaretPosition(console.getDocument().getLength());
		}

	}
	public static void main(String[] args) {
		new ServerMain();
	}

}
