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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

	public ServerMain() {
		super("Start Game");

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(350, 570));

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

		refreshRate = new JSlider(0, 1000);
		refreshRate.setMajorTickSpacing(250);
		refreshRate.setMinorTickSpacing(25);
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

				// Makes a new thread, which deals with the server

				new Thread(new Runnable() {
					public void run() {
						server = new Server(playerNum.getValue(), refreshRate.getValue());
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
				// Tell the server to end
				server.stop();
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

		JButton save = new JButton("Save");

		save.setEnabled(false); //Disabled until  a game has begun
		
		// When clicked, save the game state using Storage class
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Storage.saveState(server.getGamestate());
			}
		});
		JButton load = new JButton("Load");
		
		load.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Storage.loadState();
			}
		});

		// Save button is at 0,6
		c.gridx = 0;
		c.gridy = 6;
		add(save, c);

		// Load button is at 1,6
		c.gridx = 1;
		c.gridy = 6;
		add(load, c);

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

		// Console is at 0, 7
		c.gridx = 0;
		c.gridy = 7;
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
			
		}
		
	}
	public static void main(String[] args) {
		new ServerMain();
	}

}
