package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;


class ClientSelectDialog extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private final String EXAMPLESERVER = "eg 127.0.0.1"; //Used in the server text box
	private final int MAXCOLORS = 5; //Will be used to make a palette of size MAXCOLORS x MAXCOLORS
	private final int WIDTH = 200; //Width of the textboxes
	private JPanel spacesuitPanel; //To dynamically update from colorChooser
	private Color chosenColor;
	private BufferedImage spacesuitImage;
	
	public ClientSelectDialog()
	{		
		super("Player info");
		
		//Setup layouts
		setLayout(new MigLayout("wrap"));
		
		setPreferredSize(new Dimension(350, 350));

        
		//Pre load our spacesuit image
		loadImage();
	
		//Setup the title
		addWelcomeText();
		
		//Setup name textbox
		addName();
		
		//Setup IP address entry
		addConnectionBox();

		//Setup color chooser
		addColorPallete();
		
		//Setup preview panel
		addColorPreview();

		//Setup start game button
		addStart();
		
		pack();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		//Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2, (size.height - getHeight()) / 2, getWidth(), getHeight());
		
		
		setVisible(true);
	}


	private void addWelcomeText() {
		JLabel title = new JLabel("Welcome to Lunarcy");	
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));
		add(title, "cell 0 0 2 1, align center");
	}
	
	private void addName() {
		JLabel nameLabel = new JLabel("Enter your name:");	
		JTextField name = new JTextField("");
		//Width of 200, Height of the font size
		name.setPreferredSize(new Dimension(WIDTH, name.getFont().getSize()+5));
		
		add(nameLabel, "cell 0 1");
		add(name, "cell 1 1");
	}

	private void addConnectionBox(){
		JLabel addressLabel = new JLabel("Enter the server IP:");		
		
		final JTextField address = new JTextField(EXAMPLESERVER);
		
		//Width of 200, height of the font height
		address.setPreferredSize(new Dimension(WIDTH, address.getFont().getSize()+5));
		
		//When the textbox is clicked, clear the default text.
		address.addMouseListener(new MouseAdapter() {
		
			@Override
			public void mouseClicked(MouseEvent e) {
				if(address.getText().equals(EXAMPLESERVER)){
					address.setText("");
				}
			}
			
		});
		
		//Add the title and text box into our main dialog
		add(addressLabel, "cell 0 2");
		add(address, "cell 1 2");
	}
	

	private void addColorPallete() {
		JLabel colorLabel = new JLabel("Choose your colour:");	 
				
		JPanel colorPalette = new JPanel(new GridLayout(MAXCOLORS, MAXCOLORS));		
		
		//Create labels for N colors,
		//Use random colors to lessen the chance of two clients selecting the same colored player
		for(int i=0; i< MAXCOLORS; i++){
			for (int j = 0; j < MAXCOLORS; j++) {
				JLabel label = makeLabel(new Color((float)Math.random(), (float)Math.random(), (float)Math.random())); //Create a label of a random color
				colorPalette.add(label);
			}
		}
		colorPalette.setPreferredSize(new Dimension(spacesuitImage.getWidth(), spacesuitImage.getHeight()));		
		add(colorLabel, "cell 0 3 2 1, align center");
		add(colorPalette, "cell 0 4, span 2, align center");
		
	}
	
	private void addColorPreview(){
		//Make a new JPanel, which will display our preview image
		spacesuitPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //First draw the image
                g.drawImage(spacesuitImage, 0,0, this);

        		//Next draw a tint over the image if a color has been chosen
        		if(chosenColor!=null){
        			tintPicture(g);
        		}

            }
            
        	private void tintPicture(Graphics g) {
        		for(int x=0; x<spacesuitImage.getWidth(); x++){
        			for(int y=0; y<spacesuitImage.getHeight(); y++){
        				int pixel = spacesuitImage.getRGB(x,y);
        				//If the pixel is not transparent
        				if( (pixel>>24) != 0x00 ) {
        					//Draw a transparent rect in this pixels location
        					g.setColor(new Color(chosenColor.getRed()/255f, chosenColor.getGreen()/255f, chosenColor.getBlue()/255f, .1f));
        					g.drawRect(x, y, 1, 1);
        				}
        			}
        		}
        	}

        };
        spacesuitPanel.setPreferredSize(new Dimension(spacesuitImage.getWidth(), spacesuitImage.getHeight()));
        
        add(spacesuitPanel, "cell 0 4");
	}

	private void addStart() {
		JButton start = new JButton();
		start.setText("Join game");
		add(start, "cell 0 5 2 1, align center");		
	}
	

	private JLabel makeLabel(final Color col){
		//final so we can access it from the MouseAdaptor below
		final JLabel label = new JLabel();
		
		//Set our labels background to the color
		label.setBackground(col);
		
		//Make sure its visible
		label.setOpaque(true);
		
		//Size the label
		label.setPreferredSize(new Dimension(15, 15));
		
		//Show a border on hover, and When clicked set the chosen color and update preview
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
				spacesuitPanel.repaint(); //Update our preview
			}
		});
		return label;
	}
	
	
	private void loadImage(){
		try {
			spacesuitImage = ImageIO.read(new File("assets/items/space_suit.png")); //NEED A CREATIVE COMMONS IMAGE
		} catch (IOException e) {
			//Error loading image
			return;
		}
	}


	public static void main(String[] args) {
		new ClientSelectDialog();
	}
	
	

}
