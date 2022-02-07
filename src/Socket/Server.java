package Socket;

import static Socket.ClientHandler.os;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.PooledConnection;

import com.mysql.cj.exceptions.RSAException;

import Event.Participants;
import Payment.Pay;
import People.Person;
import Vehicle.Boat;
import Vehicle.Message;

public class Server {
	private static final int PORT = 9090;

	private static Connection connection;
	private static Statement statement;

	public static void initializeConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		statement = connection.createStatement();
	}

	public static void checkLogin(String userName, String passWord) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try { 
			ResultSet rs = statement.executeQuery("SELECT * FROM Person WHERE UserName =\"" + userName + "\" AND PassWord = \"" + passWord + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord, rs.getInt("Manager")));
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

	public static void checkUserExistance(String userName) throws SQLException, ClassNotFoundException, IOException {
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

	//TODO ASK
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
	
	public static void addPayment (String cf, Integer id_boat, String expiration, Integer id_competition, String description) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO PaymentHistory(CF, ID_Boat, Expiration, ID_Competition, Description) VALUES (?,?,?,?,?)");
			statement.setString(1, cf);
			statement.setInt(2, id_boat);
			statement.setString(3, expiration);
			statement.setInt(4, id_competition);
			statement.setString(5, description);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("addPayment Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void addBoat (String cf, String boatName, Double boatLength) throws SQLException, ClassNotFoundException, IOException {
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
	
	public static void retrievePaymentMethods(String CF) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM PaymentMethods WHERE CF = \""+ CF + "\";");
			while (rs.next()) {
				os.writeObject(new Pay(rs.getInt("ID"),rs.getString("CF"),rs.getString("CreditCard_ID"),rs.getString("Expiration"),rs.getString("CV2"),rs.getString("IBAN")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("retrievePaymentMethods Error: " + e.getMessage());
		}
		disconnect();
	}
	
	public static void retrieveBoats(String CF) throws SQLException, ClassNotFoundException, IOException {
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

	public static void retriveCompetitions() throws SQLException, ClassNotFoundException, IOException {
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
			ResultSet rs = stmt.executeQuery("SELECT C.Name, C.ID, C.Cost, B.Name AS BoatName, B.ID AS BoatID FROM Competition AS C, Boat AS B, Participants AS P WHERE P.ID_Boat = B.ID AND P.ID_Competition = C.ID ;");
			while (rsI.next()) {
				Integer controllerInteger = 0;
				rs.beforeFirst();
				while (rs.next()) {
					if (rs.getInt("ID") == rsI.getInt("ID")) {
						controllerInteger = 1;
						os.writeObject(new Participants(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"),rs.getString("BoatName"),rs.getInt("BoatID")));
						os.flush();
					}
				} 
				if (controllerInteger == 0) {
					os.writeObject(new Participants(rsI.getString("Name"),rsI.getInt("ID"),rsI.getDouble("Cost"),null, null));
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

	public static void subscriptEvent(Integer eventID, Integer boatID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants");
			while (rs.next() == true) {
				if (eventID.equals(rs.getInt("ID_Competition"))) {
					os.writeObject(new Message("CSE"));
					os.flush();
					disconnect();
					return;
				} if (boatID.equals(rs.getInt("ID_Boat"))) {
					os.writeObject(new Message("BSE"));
					os.flush();
					disconnect();
					return;
				}
			}
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existance Error: " + e.getMessage());
		}
		Statement statement = connection.createStatement();
		try {
			statement.executeUpdate("INSERT INTO Participants VALUES (" + boatID + ", " + eventID + ");");
			os.writeObject(new Message("OK"));
			os.flush();
		} catch (Exception e) {
			System.err.println("Insert sendSubscription Error: " + e.getMessage());
		}
		disconnect();
	}

	public static void checkBoat(String CF, String boatName) throws SQLException, ClassNotFoundException, IOException {
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

	public static void removeBoat(Integer ID, Integer Checker) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
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
	
	public static void retrievePerson(String CF) throws ClassNotFoundException, IOException, SQLException, InterruptedException  {
		initializeConnection();
		try {
			ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Person WHERE CF = \"" + CF + "\";");
			if (rs.next()) {
				os.writeObject(new Person(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), rs.getString("UserName"), rs.getString("PassWord"), rs.getInt("Manager")));
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
