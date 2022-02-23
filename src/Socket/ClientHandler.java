package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

	private Socket clientThread;
	private ObjectOutputStream os;
	private BufferedReader is;
	
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
					Server.checkLogin(os, command[1], command[2]);
				} else if (command[0].equals("retrieveBoats")) {
					Server.retrieveBoats(os, command[1]);
				} else if (command[0].equals("registration")) {
					Server.addPartner(command[1], command[2], command[3], command[4], Integer.parseInt(command[5]), command[6], command[7]);
				} else if (command[0].equals("checkUser")) {
					Server.checkUserExistance(os, command[1]);
				} else if (command[0].equals("retrieveCompetitions")) {
					Server.retriveCompetitions(os, command[1]);
				} else if (command[0].equals("checkEvent")) {
					Server.checkEvent(os, Integer.parseInt(command[1]), Integer.parseInt(command[2]), command[3]);
				}else if (command[0].equals("addEvent")) {
					Server.addEvent(os, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				}else if (command[0].equals("checkBoat")) {
					Server.checkBoat(os, command[1], command[2]);
				}else if (command[0].equals("removeBoat")) {
					Server.removeBoat(os, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
				}else if (command[0].equals("retrievePerson")) {
					Server.retrievePerson(os, command[1]);
				}else if (command[0].equals("addPaymentMethod")) {
					Server.addPaymentMethod(command[1], command[2], command[3], command[4], command[5]);
				}else if (command[0].equals("addPayment")) {
					Server.addPayment(command[1], Integer.parseInt(command[2]), command[3], command[4], Integer.parseInt(command[5]), command[6], command[7]);
				}else if (command[0].equals("addBoat")) {
					Server.addBoat(os, command[1], command[2], Double.parseDouble(command[3]));
				}else if (command[0].equals("retrievePaymentMethods")) {
					Server.retrievePaymentMethods(os, command[1]);
				}else if (command[0].equals("deleteSubscription")) {
					Server.deleteSubscription(os, Integer.parseInt(command[1]));
				}else if (command[0].equals("getAllParticipants")) {
					Server.getAllParticipants(os, Integer.parseInt(command[1]));
				}else if (command[0].equals("setPodium")) {
					Server.setPodium(Integer.parseInt(command[1]), command[2]);
				}
			}
		} catch (IOException | ClassNotFoundException | SQLException | NumberFormatException | InterruptedException e) {
			System.out.println("[SERVER] Closing ClientHandler " + e.getMessage());
		} finally {
			
		}
		
	}
}
