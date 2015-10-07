package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import control.Client;

public class ClientSplashScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	Client client;

	public ClientSplashScreen(Client client) {

		this.client = client;

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(400, 200));

		// Adds the title and subtext
		addLoadingText();

		// Adds a progress bar
		addLoadingBar();

		pack();

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2, (size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);
		setResizable(false);
	}

	private void addLoadingText() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JLabel title = new JLabel("Connecting to server");
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 25));

		// Main title is at 0,0
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 10, 0);

		add(title, c);

	}

	private void addLoadingBar() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally

		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		bar.setString("Waiting for players to connect..");

		// Show the percentage complete
		bar.setStringPainted(true);

		c.gridx = 0;
		c.gridy = 1;

		add(bar, c);
	}

	public void startClient() {

				// Once the clients been constructed we can hide this window
				setVisible(false);

				// Tell the client to start listening
				client.listenForGameUpdates();//TODO transfer new thread into client


	}

}
