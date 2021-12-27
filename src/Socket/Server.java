package Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private static String[] names = {"Scemo Pagliaccio"};
	private static final int PORT = 9090;
	
	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static int nThreads ;
	private static ExecutorService pool = Executors.newFixedThreadPool(nThreads = 4);
	
	public static void main(String[] args) throws IOException {
		ServerSocket listener = new ServerSocket(PORT);
		
		while (true) {
			System.out.println("[Server] Connettiti Merda");
			Socket client = listener.accept();
			System.out.println("[Server] Connesso");
			ClientHandler clientThread = new ClientHandler(client);
			clients.add(clientThread);
			
			pool.execute(clientThread);
		}
	}
	
}
