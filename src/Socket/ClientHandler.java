package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

	private Socket clientThread;
	public static ObjectOutputStream os;
	private static BufferedReader is;
	
	public ClientHandler(Socket client) throws IOException {
		this.clientThread = client;
		is = new BufferedReader(new InputStreamReader(clientThread.getInputStream()));
		os = new ObjectOutputStream(clientThread.getOutputStream());
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
				} else if (command[0].equals("checkUser")) {
					Server.checkUserExistance(command[1]);
				} else if (command[0].equals("retrieveCompetitions")) {
					Server.retriveCompetitions();
				} else if (command[0].equals("subscriptEvent")) {
					Server.subscriptEvent(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				} else if (command[0].equals("checkBoat")) {
					Server.checkBoat(command[1], command[2]);
				} else if (command[0].equals("removeBoat")) {
					Server.removeBoat(Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				}else if (command[0].equals("retrievePerson")) {
					Server.retrievePerson(command[1]);
				}
			}
		} catch (IOException | ClassNotFoundException | SQLException | NumberFormatException | InterruptedException e) {
			System.out.println("[SERVER] Closing ClientHandler");
		} finally {
			
		}
		
	}
}
