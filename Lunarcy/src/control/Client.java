package control;

import game.Direction;
import game.GameState;
import ui.Frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



//import com.sun.corba.se.impl.ior.NewObjectKeyTemplateBase;

public class Client {
	// Network related fields
		private String serverAddr;
		private static final int DEFAULT_PORT = 58627;
		private Socket socket;
		private ObjectInputStream inputFromServer;
		private ObjectOutputStream outputToServer;
		private int id;
		private String name;
		private Frame frame;

		// test constructor
		public Client() {
			id = 0;
		}

		public Client(String serverAddr, String name, boolean hardwareRenderer){
			this.serverAddr = serverAddr;
			try {
				socket = new Socket(serverAddr, DEFAULT_PORT);
				outputToServer = new ObjectOutputStream(socket.getOutputStream());
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				System.out.println("Couldn't establish connection");
				e.printStackTrace();
			}
			writeObject(name);
			System.out.println("Name sent to server: " + name);
			readInt();
			//listen for gamestates from the server
			//this will be replaced with actionlisteners
			testClientControls();
			listenForGameUpdates();
		}



		private void listenForGameUpdates() {
			while(true){
				GameState state = null;
				try {
					System.out.println("attempting to listen to client");
					state = (GameState)inputFromServer.readObject();
				} catch (IOException e) {

				} catch(ClassNotFoundException e){
					e.printStackTrace();
				}
				if(state != null)frame.getCanvas().setGameState(state);
			}
		}

		private void testClientControls() {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					Scanner sc = new Scanner(System.in);
					System.out.println("Enter command: ");
					outerloop:
					while(true){
						while(sc.hasNext()){
							String input = sc.next();
							if(input.equals("quit"))break outerloop;
							switch (input) {
							case "w":
								writeObject(new MoveAction(id,Direction.North));
								break;
							case "a":
								writeObject(new MoveAction(id,Direction.West));
								break;
							case "s":
								writeObject(new MoveAction(id,Direction.South));
								break;
							case "d":
								writeObject(new MoveAction(id,Direction.East));
								break;
							case "q":
								writeObject(new PickupAction(id,(int)(Math.random()*100))); //TODO this is placeholder for random object
								break;
							case "e":
								writeObject(new DropAction(id,(int)(Math.random()*100))); //TODO this is placeholder for random object
								break;
							case "o":
								writeObject(new OrientAction(id,true)); //TODO this is placeholder for random object
								break;
							default:
								break;
							}
						}
					}
				}
			}).start();
		}

		private void readInt() {
			System.out.println("trying to read ID");
			try {
				id = inputFromServer.readInt();
				System.out.println("My clientID is: " + id);
			} catch (IOException e) {
				System.err.println("cant read ID");
				e.printStackTrace();
			}
		}

		private boolean writeObject(Object o){
			if(o != null){
				try {
					outputToServer.writeObject(o);
					outputToServer.flush();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		public int getPlayerID() {
			return id;
		}

		public static void main(String[] args){
			new Client("localhost","JP" + (int)(Math.random()*100),true);
		}
}

