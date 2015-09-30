package ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * A GUI window for starting a new server.
 *
 * @author Ben
 *
 */
public class ServerMain extends JFrame {

	private static final long serialVersionUID = 1L;

	public ServerMain(){
		super("Start Game");

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(370, 520));

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Center the window on the screen
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((size.width - getWidth()) / 2,
				(size.height - getHeight()) / 2, getWidth(), getHeight());

		setVisible(true);

	}

	public static void main(String[] args) {
		new ServerMain();
	}



}
