package Socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.mysql.cj.protocol.x.Notice;

import Objects.Boat;
import Objects.Message;
import Objects.Notification;
import Objects.Participant;
import Objects.PaymentMethod;
import Objects.PayIstance;
import Objects.Person;

public class Server {
	private static final int PORT = 9090;

	private static Connection connection;
	private static Statement statement;

	public static void initializeConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		statement = connection.createStatement();
	}

	public static void checkLogin(ObjectOutputStream os, String userName, String passWord) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Person WHERE UserName =\"" + userName + "\" AND PassWord = \"" + passWord + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"), userName, passWord, rs.getInt("Manager")));
				os.flush();
			} else {
				os.writeObject(null);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("checkLogin Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void checkUserExistance(ObjectOutputStream os, String userName) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Person WHERE UserName =\"" + userName + "\";");
			if (rs.next()) {
				os.writeByte(1);
				os.flush();
			} else {
				os.writeByte(0);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("checkUserExistance Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addPartner (String name, String surname, String address, String cf, Integer id_club, String username, String password) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Person(Name, Surname, Address, CF, ID_Club, UserName, PassWord) VALUES (?,?,?,?,?,?,?)");
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, address);
			statement.setString(4, cf);
			statement.setInt(5, id_club);
			statement.setString(6, username);
			statement.setString(7, password);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("addPartner Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void emptyNotifications() throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM Notification");
			statement.execute();
		} catch (SQLException e) {
			System.out.println("DeleteAll Error: " + e.getMessage());
		}
		disconnect();
	}
	
	public static void checkNotifications (ObjectOutputStream os, String CF) throws SQLException, ClassNotFoundException, IOException, ParseException {
		Server.emptyNotifications();
		
		initializeConnection();
		
		Statement stmt = connection.createStatement();

		Date currDate = new Date(System.currentTimeMillis());

		try {
			ResultSet rs = stmt.executeQuery("SELECT P.ID, P.CF, B.Name AS BoatName, P.currDate, P.Expiration, C.Name AS CompName, P.Description, P.Amount, P.PaymentMethod	"
					+ "FROM PaymentHistory P "
					+ "LEFT JOIN Boat B ON B.ID = P.ID_Boat LEFT JOIN competition C ON C.ID = P.ID_Competition "
					+ "WHERE P.CF = \""+ CF + "\" AND P.Description <> \"Competition Addon\" AND currDate IN (SELECT MAX(currDate) FROM PaymentHistory GROUP BY ID_Boat, ID_Competition);");
			Integer checkInteger = 0;
			while (rs.next()) {
				if ( - currDate.getTime() + rs.getDate("Expiration").getTime() < Long.parseLong("2678400000")) {
					checkInteger = 1;
					String descriptionString = "";
					if (rs.getString("Description").equals("Boat Addon")) {
						descriptionString = "Boat Storage Fee Needs To Be Renewed";
					} else if (rs.getString("Description").equals("Membership Registration")) {
						descriptionString = "Registration Fee Is Expiring";
					}
					PreparedStatement statement = connection.prepareStatement("INSERT INTO Notification(stringObject, Description, remDays, ID_Payment) VALUES (?,?,?,?)");
					statement.setString(1, rs.getString("Description"));
					statement.setString(2, descriptionString);
					statement.setInt(3, (int) TimeUnit.MILLISECONDS.toDays(-currDate.getTime()+rs.getDate("Expiration").getTime()));
					statement.setInt(4, rs.getInt("ID"));
					statement.execute();
				}
			} if (checkInteger == 1) {
				os.writeByte(1);
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("checkNotifications Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addPaymentMethod (String cf, String creditcard_id, String expiration, String cv2, String iban) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("Select * FROM PaymentMethods WHERE CreditCard_ID = " + creditcard_id + ";");
			if (!rs.next()) {
				try {
					PreparedStatement statement = connection.prepareStatement("INSERT INTO PaymentMethods(CF, CreditCard_ID, Expiration, CV2, IBAN) VALUES (?,?,?,?,?)");
					statement.setString(1, cf);
					statement.setString(2, creditcard_id);
					statement.setString(3, expiration);
					statement.setString(4, cv2);
					statement.setString(5, iban);
					statement.execute();
				} catch (SQLException e) {
					System.out.println("addPayment Error: " + e.getMessage());
				}
			}
		} catch (SQLException e) {
			System.out.println("retrieve-ALL-Competitions Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addPayment (String cf, Integer id_boat, String date, String expiration, Integer id_competition, String description, Double amount, String payment) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO PaymentHistory(CF, ID_Boat, currDate, Expiration, ID_Competition, Description, Amount, PaymentMethod) VALUES (?,?,?,?,?,?,?,?)");
			statement.setString(1, cf);
			if (id_boat == 0) {
				statement.setString(2, null);
			} else {
				statement.setInt(2, id_boat);
			}
			statement.setString(3, date);
			if (description.equals("Competition Addon")) {
				statement.setString(4, null);
			} else {
				statement.setString(4, expiration);
			}
			if (id_competition == 0) {
				statement.setString(5, null);
			} else {
				statement.setInt(5, id_competition);
			}
			statement.setString(6, description);
			statement.setDouble(7, amount);
			statement.setString(8, payment);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("addPayment Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addBoat (ObjectOutputStream os, String cf, String boatName, Double boatLength) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Boat(CF_Owner, Name, Length) VALUES (?,?,?)");
			statement.setString(1, cf);
			statement.setString(2, boatName);
			statement.setDouble(3, boatLength);
			statement.execute();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE Name =\"" + boatName + "\";");
			rs.next();
			os.writeObject(new Message(rs.getString("ID")));
			os.flush();
		} catch (SQLException e) {
			System.out.println("addBoat Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retrievePaymentMethods(ObjectOutputStream os, String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM PaymentMethods WHERE CF = \""+ CF + "\";");
			while (rs.next()) {
				os.writeObject(new PaymentMethod(rs.getInt("ID"),rs.getString("CF"),rs.getString("CreditCard_ID"),rs.getString("Expiration"),rs.getString("CV2"),rs.getString("IBAN")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrievePaymentMethods Error: " + e.getMessage());
		}
		disconnect();
	}
	
	public static void getNotifications(ObjectOutputStream os) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT N.stringObject, N.Description, N.remDays, P.Amount FROM Notification N, PaymentHistory P WHERE P.ID = N.ID_Payment;");
			while (rs.next()) {
				os.writeObject(new Notification(rs.getString("stringObject"), rs.getString("Description"), rs.getInt("remDays"), rs.getDouble("Amount")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("getNotification Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retrievePaymentHistory(ObjectOutputStream os, String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT P.ID, P.CF, B.Name AS BoatName, P.currDate, P.Expiration, C.Name AS CompName, P.Description, P.Amount, P.PaymentMethod \r\n"
					+ "FROM PaymentHistory P LEFT JOIN Boat B ON B.ID = P.ID_Boat LEFT JOIN competition C ON C.ID = P.ID_Competition\r\n"
					+ "WHERE P.CF = \""+ CF + "\";");
			while (rs.next()) {
				os.writeObject(new PayIstance(rs.getInt("ID"),rs.getString("CF"),rs.getString("BoatName"),rs.getString("currDate"),rs.getString("Expiration"),rs.getString("CompName"),rs.getString("Description"),rs.getDouble("Amount"),rs.getString("PaymentMethod")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrievePaymentMethods Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retrieveBoats(ObjectOutputStream os, String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE CF_Owner = \""+ CF + "\";");
			while (rs.next()) {
				os.writeObject(new Boat(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Length")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrieveBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retriveCompetitions(ObjectOutputStream os, String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmtI = connection.createStatement();
		ResultSet rsI = null;
		try {
			rsI = stmtI.executeQuery("Select * FROM Competition");
		} catch (SQLException e) {
			System.out.println("retrieve-ALL-Competitions Error: " + e.getMessage());
		}
		Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		try {
			ResultSet rs = stmt.executeQuery("SELECT C.Name, C.ID, C.Cost, C.Date, C.WinPrice, C.Podium, B.Name AS BoatName, B.ID AS BoatID FROM Competition AS C, Boat AS B, Participants AS P WHERE P.ID_Boat = B.ID AND P.ID_Competition = C.ID AND B.CF_Owner = \"" + CF + "\";");
			while (rsI.next()) {
				Integer controllerInteger = 0;
				rs.beforeFirst();
				while (rs.next()) {
					if (rs.getInt("ID") == rsI.getInt("ID")) {
						controllerInteger = 1;
						os.writeObject(new Participant(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"),rs.getString("Date"),rs.getDouble("WinPrice"),rs.getString("Podium"),rs.getString("BoatName"),rs.getInt("BoatID")));
						os.flush();
					}
				} 
				if (controllerInteger == 0) {
					os.writeObject(new Participant(rsI.getString("Name"),rsI.getInt("ID"),rsI.getDouble("Cost"),rsI.getString("Date"),rsI.getDouble("WinPrice"),rsI.getString("Podium"),null, null));
					os.flush();
				}
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retriveCompetitions Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void checkEvent(ObjectOutputStream os, Integer eventID, Integer boatID, String CF) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants, Boat WHERE Boat.ID = Participants.ID_Boat AND Boat.CF_Owner = \"" + CF + "\"");
			while (rs.next() == true) {
				if (eventID == rs.getInt("ID_Competition")) {
					os.writeObject(new Message("CSE"));
					os.flush();
					disconnect();
					return;
				} else if (boatID == rs.getInt("ID_Boat")) {
					os.writeObject(new Message("BSE"));
					os.flush();
					disconnect();
					return;
				}
			}
			os.writeObject(new Message("OK"));
			os.flush();
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existance Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void getAllParticipants(ObjectOutputStream os, Integer eventID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants WHERE ID_Competition = \"" + eventID + "\";");
			while (rs.next() == true) {
				os.writeObject(new Message(String.valueOf(rs.getInt("ID_Boat"))));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (Exception e) {
			System.err.println("getAllParticipants Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void setPodium(Integer id, String podium) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE competition SET Podium = \"" + podium + "\" WHERE `competition`.`ID` = " + id + ";");
			statement.execute();
		} catch (Exception e) {
			System.err.println("setPodium Error: " + e.getMessage());
		}
		disconnect();
	}


	public static void deleteSubscription(ObjectOutputStream os, Integer eventID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants WHERE ID_Competition = " + eventID + ";");
			while (rs.next() == true) {
				PreparedStatement stmtI = connection.prepareStatement("DELETE FROM Participants WHERE ID_Competition = " + eventID + ";");
				stmtI.executeUpdate();
				os.writeObject(new Message("OK"));
				os.flush();
				disconnect();
				return;
			}
			os.writeObject(new Message("NotSub"));
			os.flush();		
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existance Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addEvent(ObjectOutputStream os, Integer eventID, Integer boatID) throws SQLException, ClassNotFoundException {
		initializeConnection();
		Statement statement = connection.createStatement();
		try {
			statement.executeUpdate("INSERT INTO Participants(ID_Boat, ID_Competition) VALUES (" + boatID + "," + eventID + ");");
			os.writeObject(new Message(String.valueOf(boatID) + "#" + String.valueOf(eventID)));
			os.flush();
		} catch (Exception e) {
			System.err.println("Insert sendSubscription Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void checkBoat(ObjectOutputStream os, String CF, String boatName) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Boat WHERE CF_Owner = \"" + CF + "\" AND Name = \"" + boatName + "\";");
			if (rs.next()) {
				os.writeObject(new Message("KO"));
				os.flush();
			}
			os.writeObject(new Message("OK"));
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrieveBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void removeBoat(ObjectOutputStream os, Integer ID, Integer Checker) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
		initializeConnection();
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Participants WHERE ID_Boat = " + ID + ";");
			if (rs.next() && Checker == 0) {
				os.writeObject(new Message("RQDL"));
				os.flush();
			} if (Checker == 1) {
				PreparedStatement stmtI = connection.prepareStatement("DELETE FROM Participants WHERE ID_Boat = " + ID + ";");
				stmtI.executeUpdate();
			}
			PreparedStatement stmt = connection.prepareStatement("DELETE FROM Boat WHERE ID = \""+ ID + "\"");
			stmt.executeUpdate();
			os.writeObject(new Message("OK"));
			os.flush();
		} catch (SQLException e) {
			System.out.println("removeBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void retrievePerson(ObjectOutputStream os, String CF) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
		initializeConnection();
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Person WHERE CF = \"" + CF + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"), rs.getString("UserName"), rs.getString("PassWord"), rs.getInt("Manager")));
				os.flush();
			}
		} catch (SQLException e) {
			System.out.println("removeBoats Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void disconnect() throws SQLException {
		connection.close();
		return;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		LinkedList<ClientHandler> clients = new LinkedList<>();
		//LoggedUsers = new LinkedList<>();
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			while (true) {
				System.out.println("[SERVER] Server Is Waiting For A Connection");
				Socket client = server.accept();
				System.out.println("[SERVER] Client Connected");

				ClientHandler clientThread = new ClientHandler(client);
				clients.add(clientThread);
				new Thread(clientThread).start();
				System.out.println(clients.size());
			}
		} catch (IOException e) {
			System.out.println("[SERVER] Client Disconnected");
		} finally {
			try {
				server.close();
			} catch (Exception e) {
				System.err.println("[SERVER] Closing ERROR " + e.getMessage());
			}
		}
	}
}
