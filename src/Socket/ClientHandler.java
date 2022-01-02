package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

	private Socket client;
	private PrintWriter out;
	private static BufferedReader is;
	
	public ClientHandler(Socket clientSocket) throws IOException {
		this.client = clientSocket;
		is = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream());
	}

	@Override
	public void run() {
		try {
			while(true) {
				String[] command = is.readLine().split("#");
				if (command[0].equals("connect")) {
					Server.checkLogin(command[1], command[2]);
				} else if (command[0].equals("retrieveBoats")) {
					Server.retrieveBoats(command[1]);
				} else if (command[0].equals("registration")) {
					Server.addPartner(command[1], command[2], command[3], command[4], Integer.parseInt(command[5]), command[6], command[7]);
				} else if (command[0].equals("check")) {
					Server.checkUserExistance(command[1]);
				} else if (command[0].equals("retrieveCompetitions")) {
					Server.retriveCompetitions();
				} else if (command[0].equals("subscriptEvent")) {
					Server.sendSubscription(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				}
			}
		} catch (IOException | ClassNotFoundException | SQLException e) {
			System.out.println("[SERVER] Closing ClientHandler");
		} finally {
			out.close();
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
