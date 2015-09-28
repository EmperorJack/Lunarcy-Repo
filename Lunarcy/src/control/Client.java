package control;

import game.Direction;

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

		Client(String serverAddr, String name){
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
			System.out.println("have written name" + name);
			readID();
			System.out.println("have read ID"+ id);
			//this will be replaced with actionlisteners
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter command");
			outerloop:
			while(true){
				while(sc.hasNext()){
					String input = sc.next();
					if(input.equals("q"))break outerloop;
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
					case "p":
						writeObject(new PickupAction(id,(int)(Math.random()*100))); //TODO this is placeholder for random object
						break;
					default:
						break;
					}
				}
			}
			//close socket
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		private void readID() {
			System.out.println("trying to read ID");
			try {
				id = inputFromServer.readInt();
				System.out.println("My ID " + id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("cant read ID");
				e.printStackTrace();
			}
		}

		boolean writeObject(Object o){
			if(o != null){
				try {
					outputToServer.writeObject(o);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		public static void main(String[] args){
			new Client("localhost","JP" + (int)(Math.random()*100));
		}
}

