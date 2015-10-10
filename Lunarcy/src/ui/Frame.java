package ui;

import game.GameState;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import control.Client;

/**
 * Maintains the Swing window for the Lunarcy game. Handles the confirmation
 * dialog that appears when a user attempts to close their client. Contains the
 * Processing canvas component that handles all rendering of the game state.
 *
 * @author Jack & Ben
 *
 */
@SuppressWarnings("serial")
public class Frame extends JFrame {

	// the size the frame should be drawn at
	public final int WIDTH;
	public final int HEIGHT;

	// the processing canvas
	private final Canvas canvas;

	public Frame(Client client, GameState gameState, int width, int height,
			boolean hardwareRenderer) {
		
		this.WIDTH = width;
		this.HEIGHT = height;
		
		setTitle("Lunarcy");
		setSize(WIDTH, HEIGHT);

		// Catch exit with confirmation dialog
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane
						.showConfirmDialog(null, new JLabel(
								"Are you sure you want to exit Lunarcy?"),
								"Exit Game", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);
				// Only close if they confirm they want to close
				if (option == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		// setup processing canvas
		setLayout(new BorderLayout());
		canvas = new Canvas(WIDTH, HEIGHT, client, gameState, hardwareRenderer);
		add(canvas, BorderLayout.CENTER);

		// Center align the frame on screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2,
				(size.height - getHeight()) / 2, getWidth(), getHeight());

		setResizable(false);
		setVisible(true);

		// run the processing canvas
		canvas.init();
	}

	public Canvas getCanvas() {
		return canvas;
	}
}