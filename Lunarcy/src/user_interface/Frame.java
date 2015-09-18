package user_interface;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class Frame extends JFrame {

	public static final int INIT_WIDTH = 1280;
	public static final int INIT_HEIGHT = 720;

	public Frame() {
		setTitle("Lunarcy");
		setSize(INIT_WIDTH, INIT_HEIGHT);
		// TODO catch exit with confirmation dialog
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// setup processing canvas panel
		JPanel panel = new JPanel(new BorderLayout());
		Canvas canvas = new Canvas(INIT_WIDTH, INIT_HEIGHT);
		panel.add(canvas, BorderLayout.CENTER);
		add(panel);

		// run the processing canvas
		canvas.init();

		// setup resize listener
		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				// send the new window size to the canvas
				canvas.adjustScaling(e.getComponent().getWidth(), e
						.getComponent().getHeight());
			}
		});

		setVisible(true);
	}

	public static void main(String args[]) {
		new Frame();
	}
}