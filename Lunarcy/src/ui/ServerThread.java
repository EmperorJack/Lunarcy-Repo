package ui;

import storage.Storage;
import control.Server;

public class ServerThread implements Runnable {

	public Server server;
	private int maxClients;
	private int updateFreq;

	public ServerThread(int maxClients, int updateFreq) {
		this.maxClients = maxClients;
		this.updateFreq = updateFreq;
	}

	@Override
	public void run() {
		server = new Server(maxClients, updateFreq);
		server.run();
	}

	public void stop(){
		server.stop();
	}

	public void load(int maxClients, int updateFreq){
		server = new Server(maxClients, updateFreq, Storage.loadState());
	}

	public void save(){
		server.saveGamestate();
	}

}
