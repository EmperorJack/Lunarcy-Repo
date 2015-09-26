//package control;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintStream;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.Scanner;
//
//public class Client {
//	// Network related fields
//		private String serverAddr;
//		private static final int DEFAULT_PORT = 6667;
//		private Socket socket;
//		private ObjectInputStream serverIn;
//		private ObjectOutputStream serverOut;
//		
//		
//		Client(String serverAddr, String name){
//			this.serverAddr = serverAddr;
//			try {
//				socket = new Socket(serverAddr, DEFAULT_PORT);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//		
//		
//		public static void main(String[] args){
//			new Client("localhost","JP" + Math.random());
//		}
//}


/**
 * The client program for the paper scissors rock game
 * @author Jack Purvis
 * @version 1.0
 */

import java.io.*;
import java.net.Socket;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Client
{
	// UI related fields
	private JFrame clientWindow = new JFrame("Client");
	private JTextArea textOutput = new JTextArea(40,60);
	private JPanel buttonPanel = new JPanel();
	private JButton joinGameButton = new JButton("Join Game");
	private JButton disconnectButton = new JButton("Disconnect");
	private JButton quitButton = new JButton("Quit");
	private JPanel gameInterfacePanel = new JPanel();
	private JButton paperButton = new JButton("Paper");
	private JButton scissorsButton = new JButton("Scissors");
	private JButton rockButton = new JButton("Up");
	
	// Network related fields
	private String server;
	private static final int PORT = 6667;
	private Socket socket;
	private ObjectInputStream inputFromServer;
	private ObjectOutputStream outputToServer;
	
	// Game related fields
	private String userName = "";
	private boolean clientTurn = false;
	
	/**
	 * Create a new Client
	 */
	public static void main(String[] args)
	{
		new Client();
	}
	
	/**
	 * Set up the client interface
	 */
	public Client()
	{
		// Initialize the client window
		clientWindow.setSize(200,300);
		clientWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Ask the user for a user name
		while (userName.equals(""))
		{
			userName = JOptionPane.showInputDialog("Enter user name");
		}
		
		// Add the button panel
		clientWindow.add(buttonPanel, BorderLayout.NORTH);
		buttonPanel.add(new JLabel("Paper Scissors Rock Client"));
		
		// Add the join game button
		buttonPanel.add(joinGameButton);
		joinGameButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				// If there is no server already connected to
				if (server == null)
				{
					joinGame();
				}
				else
				{
					textOutput.append("Already connected to a server\n");
				}
			}
		});

		// Add the disconnect button
		buttonPanel.add(disconnectButton);
		disconnectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				if (socket != null)
				{
					textOutput.append("\nDisconnected: ");
					closeConnection();
				}
			}
		});
		
		// Add the quit button
		buttonPanel.add(quitButton);
		quitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				quit();
			}
		});
		
		// Add the user name label
		buttonPanel.add(new JLabel("Name: " + userName), BorderLayout.EAST);
		
		// Add the text area
		JScrollPane textOutputSP = new JScrollPane(textOutput);
		clientWindow.add(textOutputSP, BorderLayout.CENTER);
		
		// Add the game interface panel
		clientWindow.add(gameInterfacePanel, BorderLayout.SOUTH);
		
		// Add the paper button
		gameInterfacePanel.add(paperButton);
		paperButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				// If it's the player's turn
				if (clientTurn == true)
				{
					outputToServer.writeObject(new PickupAction(4,1234));
					textOutput.append("Your chosen move is PAPER!\n");
					clientTurn = false;
				}
			}
		});

		// Add the scissors button
		gameInterfacePanel.add(scissorsButton);
		scissorsButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				// If it's the player's turn
				if (clientTurn == true)
				{
					write("SCISSORS");
					textOutput.append("Your chosen move is SCISSORS!\n");
					clientTurn = false;
				}
			}
		});

		// Add the rock button
		gameInterfacePanel.add(rockButton);
		rockButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				//send()
			}
		});
		
		// Hide the game interface panel to begin with
		gameInterfacePanel.setVisible(false);
		
		// Show the client window
		clientWindow.pack();
		clientWindow.setVisible(true);
	}
	
	/**
	 * Attempt to connect to a server
	 */
	public void joinGame()
	{
		// Ask the user for a server name
		String serverName = JOptionPane.showInputDialog("Enter server address");

		// Attempt to make the connection
		textOutput.append("Attempting to join server " + serverName + "\n");
		try
		{
			socket = new Socket(serverName, PORT);
			inputFromServer = new ObjectInputStream(socket.getInputStream());
            outputToServer = new ObjectOutputStream(socket.getOutputStream());
            
            // Report a successful connection
            server = serverName;
            textOutput.append("Succesfully connected to " + serverName + "\n");
            
            // Begin listening to the server
            new Thread(new Runnable(){ public void run(){
                listenToServer();
            }}).start();
            
            // Send the user name to the server
            write(userName);
            
		} catch (IOException e){textOutput.append("Failed to connect " + e + "\n");}
	}
	
	/**
	 * Listen for input from the server
	 */
	public void listenToServer()
	{
		GameSate gamestate = outputToServer;
	}
	
	/**
	 * Send a message to the server
	 */
	public void write(String message)
	{
		if (!message.equals(null)){
			outputToServer.println(message);
			outputToServer.flush();
		}
	}
	
	/**
	 * Starts the game of paper scissors rock between the clients
	 */
	public void startGame()
	{
		// Enables the game interface by setting it to visible;
		gameInterfacePanel.setVisible(true);
		textOutput.append("\nThe game has started!\n");
	}
	
	/**
	 * Disconnect from the active server if it exists
	 */
	public void closeConnection()
	{
		// Close the socket
		try
		{
            if (socket != null)
            {
            	write("CQUIT");
                socket.close();
                socket = null;
            }
        } catch(IOException e){textOutput.append("Failed to close connection " + e + "\n");}
		
		 // Close the server input and output
        if (inputFromServer != null && outputToServer != null)
        {
            inputFromServer.close();
            inputFromServer = null;
            outputToServer.close();
            outputToServer = null;
        }
        
        // Reset the current server name
        server = null;
        
        textOutput.append("Connection Closed from Server\n");
	}
	
	/**
	 * First disconnect from any connected servers then quit the client program
	 */
	public void quit()
	{
		// Close the current server connection if any
		closeConnection();
		
		// Dispose of the client window
		clientWindow.dispose();
		
		// Quit the program
		System.exit(0);
	}
}

