package ui.ApplicationWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import network.Client;

/**
 * A GUI Window for starting a new Client.
 *
 * @author evansben1
 *
 */
class ClientMain extends JFrame {

	private static final long serialVersionUID = 1L;

	// The default text in the server textbox
	private final String EXAMPLESERVER = "localhost";

	// Used to make a color palette of size MAXCOLORS x MAXCOLORS
	private final int MAXCOLORS = 5;

	// Width of the textboxes
	private final int WIDTH = 200;

	// To prevent players from having excessively long names
	private final int MAX_USERNAME_LENGTH = 20;

	// To dynamically update from colorChooser
	private JPanel spacesuitPanel;
	private Color chosenColor;
	private BufferedImage spacesuitImage;

	// All need to be accessed when start button is pushed
	private JComboBox<String> renderMode;
	private JComboBox<String> resolutionMode;
	private JTextField nameTextbox;
	private JTextField serverTextbox;
	private JPanel colorPalette;

	public ClientMain() {
		super("Join Game");

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(370, 480));

		// Pre load our spacesuit image
		loadImage();

		// Setup the title
		addWelcomeText();

		// Add a horizontal line
		addSeperator(0, 1);

		// Setup name textbox
		addName();

		// Setup IP address entry
		addConnectionBox();

		// Add a horizontal line
		addSeperator(0, 4);

		// Setup color chooser
		addColorPallete();

		// Setup preview panel
		addColorPreview();

		// Add rendering option
		addRenderDropdown();

		// Add a horizontal line
		addSeperator(0, 7);

		// Setup start game button
		addStart();

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2,
				(size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);
		setResizable(false);
	}

	/**
	 * Adds a horizontal separator at the specified x,y coordinates
	 *
	 * @param x
	 *            the column
	 * @param y
	 *            the row
	 */
	private void addSeperator(int x, int y) {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JSeparator seperator = new JSeparator();

		// Pad the seperator, and set allignment based on params
		c.insets = new Insets(10, 25, 10, 25);
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = GridBagConstraints.REMAINDER;

		add(seperator, c);
	}

	private void addWelcomeText() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel title = new JLabel("Welcome to Lunarcy");
		// Make the font larger (25px)
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));

		// Left padding to centre text
		c.insets = new Insets(0, 30, 0, 0);

		// Welcome label at 0,0 taking up two cells
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;

		add(title, c);
	}

	private void addName() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel nameLabel = new JLabel("Enter your name:");
		nameTextbox = new JTextField("");

		// Width of 200, Height of the font size
		nameTextbox.setPreferredSize(new Dimension(WIDTH, nameTextbox.getFont()
				.getSize() + 5));

		// Name label goes at 0,2
		c.gridx = 0;
		c.gridy = 2;

		add(nameLabel, c);

		// Name textbox goes at 1,2
		c.gridx = 1;
		c.gridy = 2;

		add(nameTextbox, c);
	}

	private void addConnectionBox() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel addressLabel = new JLabel("Enter the server IP:");

		serverTextbox = new JTextField(EXAMPLESERVER);

		// Width of 200, height of the font height
		serverTextbox.setPreferredSize(new Dimension(WIDTH, serverTextbox
				.getFont().getSize() + 5));

		// When the textbox is clicked, clear the default text.
		serverTextbox.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (serverTextbox.getText().equals(EXAMPLESERVER)) {
					serverTextbox.setText("");
				}
			}

		});

		// Address label goes at 0,3
		c.gridx = 0;
		c.gridy = 3;
		add(addressLabel, c);

		// Address textbox goes at 1,3
		c.gridx = 1;
		c.gridy = 3;
		add(serverTextbox, c);
	}

	private void addColorPallete() {

		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel colorLabel = new JLabel("Choose your colour:");

		colorPalette = new JPanel(new GridLayout(MAXCOLORS, MAXCOLORS));

		// Create labels for N colors,
		// Use random colors to lessen the chance of two clients selecting the
		// same colored player
		for (int i = 0; i < MAXCOLORS; i++) {
			for (int j = 0; j < MAXCOLORS; j++) {
				// Create a label of a random color
				JLabel label = makeLabel(new Color((float) Math.random(),
						(float) Math.random(), (float) Math.random()));
				colorPalette.add(label);
			}
		}

		colorPalette.setPreferredSize(new Dimension(spacesuitImage.getWidth(),
				spacesuitImage.getHeight()));

		// Color label at 0,5 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		add(colorLabel, c);

		// Color pallete at 0,6 with a width of 1
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 1;
		c.insets = new Insets(0, 20, 0, 0);
		add(colorPalette, c);

	}

	private void addColorPreview() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		// Make a new JPanel, which will display our preview image
		spacesuitPanel = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// First draw the image
				g.drawImage(spacesuitImage, 0, 0, this);

				// Next draw a tint over the image if a color has been chosen
				if (chosenColor != null) {
					tintPicture(g);
				}

			}

			/**
			 * Tint the image based on the chosen colour, to show an accurate
			 * preview of the space suit.
			 *
			 * @param g
			 */
			private void tintPicture(Graphics g) {
				for (int x = 0; x < spacesuitImage.getWidth(); x++) {
					for (int y = 0; y < spacesuitImage.getHeight(); y++) {
						int pixel = spacesuitImage.getRGB(x, y);
						// If the pixel is not transparent
						if ((pixel >> 24) != 0x00) {
							// Draw a transparent rect in this pixels location
							g.setColor(new Color(chosenColor.getRed() / 255f,
									chosenColor.getGreen() / 255f, chosenColor
											.getBlue() / 255f, .1f));
							g.drawRect(x, y, 1, 1);
						}
					}
				}
			}

		};
		spacesuitPanel.setPreferredSize(new Dimension(
				spacesuitImage.getWidth(), spacesuitImage.getHeight()));

		// The panel is at cell 1,6
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(0, 25, 0, 0);

		add(spacesuitPanel, c);
	}

	private void addRenderDropdown() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel title = new JLabel("Select your rendering mode:");
		// Render type dropdown
		String[] renderOptions = new String[] { "Software", "Hardware" };
		renderMode = new JComboBox<>(renderOptions);

		JLabel resolutionTitle = new JLabel("Select your resolution:");
		// Resolution dropdown
		String[] resolutionOptions = new String[] { "Default", "640x360",
				"1024x576", "1280x720", "1366x768", "1600x900", "1920x1080" };
		resolutionMode = new JComboBox<>(resolutionOptions);

		// Title at 0,8 width a width of 2 cells
		c.gridx = 0;
		c.gridy = 8;
		c.gridwidth = 2;

		add(title, c);

		// Render Option dropdown at 0,9 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 9;

		add(renderMode, c);

		// Resolution title is at 0,10 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(5, 0, 0, 0);

		add(resolutionTitle, c);

		// Resolution Option dropdown at 0,11 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 11;
		c.insets = new Insets(0, 0, 5, 0);

		add(resolutionMode, c);

	}

	private void addStart() {
		// Setup layout
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JButton start = new JButton();
		start.setText("Join game");

		// Open frame on button push
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Exit if they have not completed all fields
				if (!validInput()) {
					return;
				}

				// get the resolution mode
				int width;
				int height;
				String mode = (String) resolutionMode.getSelectedItem();

				// if the 'default' value was selected
				if (mode.equals("Default")) {
					width = 1280;
					height = 720;
				}
				// If they selected one of the other item
				else {
					width = Integer.valueOf(mode.split("x")[0]);
					height = Integer.valueOf(mode.split("x")[1]);
				}

				// Make a new client
				Client client;

				try {
					client = new Client(serverTextbox.getText(), nameTextbox
							.getText(), chosenColor, width, height, renderMode
							.getSelectedItem().equals("Hardware"));
				}
				// if an exception is thrown entered details must have been
				// invalid
				catch (IllegalArgumentException ex) {
					JOptionPane.showMessageDialog(null,
							"Your details were invalid. Please try again");
					return;
				}

				// Hide this window
				setVisible(false);

				//Start listening for updates
				client.listenForGameUpdates();

			}

			private boolean validInput() {

				// Regex from:
				// http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
				String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
						+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

				// If no name was entered (or just spaces were used)
				if (nameTextbox.getText().trim().isEmpty()
						|| nameTextbox.getText().length() > MAX_USERNAME_LENGTH) {

					// Set a red border on the nameTextbox
					nameTextbox.setBorder(BorderFactory
							.createLineBorder(Color.RED));
					return false;
				}

				// Server can only hold localhost or an IP
				if (!serverTextbox.getText().trim().equals("localhost")
						&& !serverTextbox.getText().matches(IPADDRESS_PATTERN)) {

					// The name must be valid now, so we can remove the red
					// border
					nameTextbox.setBorder(BorderFactory
							.createLineBorder(Color.GRAY));

					// Set a red border on the serverTextbox
					serverTextbox.setBorder(BorderFactory
							.createLineBorder(Color.RED));

					return false;
				}

				if (chosenColor == null) {
					colorPalette.setBorder(BorderFactory
							.createLineBorder(Color.RED));
					return false;
				}

				return true;
			}
		});

		// Start button at 0,12 with a width of 2 cells
		c.gridx = 0;
		c.gridy = 12;
		c.gridwidth = 2;
		add(start, c);
	}

	private JLabel makeLabel(final Color col) {
		// final so we can access it from the MouseAdaptor below
		final JLabel label = new JLabel();

		// Set our labels background to the color
		label.setBackground(col);

		// Make sure its visible
		label.setOpaque(true);

		// Size the label
		label.setPreferredSize(new Dimension(15, 15));

		// Show a border on hover, and When clicked set the chosen color and
		// update preview
		label.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setBorder(null);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				chosenColor = col;
				spacesuitPanel.repaint(); // Update our preview
			}
		});
		return label;
	}

	private void loadImage() {
		try {
			spacesuitImage = ImageIO.read(new File(
					"assets/clientmain/Player.png"));
		} catch (IOException e) {
			// Error loading image
			return;
		}
	}

	public static void main(String[] args) {
		new ClientMain();
	}

}
