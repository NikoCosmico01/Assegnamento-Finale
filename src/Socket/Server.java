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

import Objects.Boat;
import Objects.Message;
import Objects.Notification;
import Objects.Participant;
import Objects.PaymentMethod;
import Objects.PayIstance;
import Objects.Person;

/**
 * This class represents the server which contains all the methods that can be called by the clientHandler (via the Client).
 * 
 * @author NicoT
 *
 */

public class Server {
	private static final int PORT = 9090;

	private static Connection connection;
	private static Statement statement;

	/**
	 * This method is called when we want to establish a connection to the mySQL Database.
	 * 
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	
	public static void initializeConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		statement = connection.createStatement();
	}

	/**
	 * This method is called at the system startup, when someone wants to login (via username and password), it checks the Existence of these
	 * credentials and returns the object {@code Person} containing all the informations about that user. If the recognized user has Manager instance equals to 1
	 * than a specific page {@code Controller_Manager} will be shown.
	 * 
	 * @param os Output Stream Output Stream
	 * @param userName written username 
	 * @param passWord written password
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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

	/**
	 * It checks whether if one user is still subscripted when someone wants to register {@code Controller_SignUp} and return a byte (1) if the
	 * user is still in the database or (0) if not.
	 * 
	 * @param os Output Stream
	 * @param userName the written username
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
	public static void checkUserExistence(ObjectOutputStream os, String userName) throws SQLException, ClassNotFoundException, IOException {
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
			System.out.println("checkUserExistence Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * It has the purpose to insert all the details of the registering user onto the database if all the fields have been properly filled.
	 * 
	 * @param name Partner name
	 * @param surname Partner surname
	 * @param address Partner address
	 * @param cf Partner CF
	 * @param username Partner userName
	 * @param password Partner passWord
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

	public static void addPartner (String name, String surname, String address, String cf, String username, String password) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO Person(Name, Surname, Address, CF, UserName, PassWord) VALUES (?,?,?,?,?,?)");
			statement.setString(1, name);
			statement.setString(2, surname);
			statement.setString(3, address);
			statement.setString(4, cf);
			statement.setString(5, username);
			statement.setString(6, password);
			statement.execute();
		} catch (SQLException e) {
			System.out.println("addPartner Error: " + e.getMessage());
		}
		disconnect();
	}

	/**
	 * It empties the Notification database table when a user logs out because it would be updated on the next login.
	 * 
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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
	
	/**
	 * It has the purpose to check if the expiring date of a precise payment is getting closer and, if yes, it will popup a notification
	 * whenever that user logs in.
	 * 
	 * @param os Output Stream
	 * @param CF passed CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws ParseException
	 */

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
			} else {
				os.writeByte(0);
			} os.flush();
		} catch (SQLException e) {
			System.out.println("checkNotifications Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * This method is called when, during the payment, the user decides to add a new payment method (both with Credit Card and IBAN).
	 * 
	 * @param cf owner CF
	 * @param creditcard_id CC Number
	 * @param expiration CC Expiration
	 * @param cv2 CC CV2 Number
	 * @param iban IBAN Number
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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
	
	/**
	 * This method is called when a payment is successful and we want it to be added into the paymentHistory database table
	 * with the relative details.
	 * 
	 * @param cf passed CF
	 * @param id_boat passed Boat ID
	 * @param date payment execution Date
	 * @param expiration payment Expiration
	 * @param id_competition passed Competition ID
	 * @param description payment description
	 * @param amount payment amount
	 * @param payment payment method
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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
	
	/**
	 * This method is called whenever a user decides to add a new boat to his list. The boat addon is executed when the
	 * boat storage fee is successfully payed.
	 * 
	 * @param os Output Stream
	 * @param cf passed CF
	 * @param boatName new Boat Name
	 * @param boatLength new Boat Length
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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

	/**
	 * This method is called all the times that the user enters the payment page and it loads all the payment methods
	 * previously added in order to avoid the manual re-insert of these values.
	 * 
	 * @param os Output Stream
	 * @param CF passed CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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
	
	/**
	 * This method is called when a user logs in, it checks if the Notification database table is populated and, if yes, it warns the user.
	 * 
	 * @param os Output Stream
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

	public static void getNotifications(ObjectOutputStream os) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT N.stringObject, N.Description, N.remDays, P.Amount, N.ID_Payment, P.ID_Boat, B.Length FROM Notification N, PaymentHistory P, Boat B WHERE P.ID = N.ID_Payment AND B.ID = P.ID_Boat;");
			while (rs.next()) {
				if (rs.getString("stringObject").equals("Boat Addon")) {
					os.writeObject(new Notification(rs.getString("stringObject"), rs.getString("Description"), rs.getInt("remDays"), rs.getDouble("Amount"), rs.getInt("ID_Boat"), rs.getDouble("Length")));
				} else if (rs.getString("stringObject").equals("Membership Registration")) {
					os.writeObject(new Notification(rs.getString("stringObject"), rs.getString("Description"), rs.getInt("remDays"), rs.getDouble("Amount"), 0, 0.0));
				}
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("getNotification Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * This method is called whenever a manager logs in and is used to display all the running competition onto the manager homepage.
	 * 
	 * @param os Output Stream
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

	public static void getEvents(ObjectOutputStream os) throws SQLException, ClassNotFoundException, IOException {
		initializeConnection();
		Statement stmt = connection.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT Name, ID, Cost, Date, WinPrice FROM Competition");
			while (rs.next()) {
				os.writeObject(new Participant(rs.getString("Name"),rs.getInt("ID"),rs.getDouble("Cost"),rs.getString("Date"),rs.getDouble("WinPrice")));
				os.flush();
			}
			os.writeObject(null);
			os.flush();
		} catch (SQLException e) {
			System.out.println("getNotification Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * This method is called when a user wants to check his payment history {@code Controller_History}.
	 * 
	 * @param os Output Stream
	 * @param CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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

	/**
	 * This method is used to retrieve all the boats owned by the logged user and put them inside the listview.
	 * 
	 * @param os Output Stream
	 * @param CF passed CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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
	
	/**
	 * This method is used to retrieve all the competitions and associate them to the corresponding boat in case that one is
	 * subscripted to any of the available competitions.
	 * 
	 * @param os Output Stream
	 * @param CF passed CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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

	/**
	 * This method is used to check whether if an event is subscriptable or it has been still subscripted with another boat.
	 * 
	 * @param os Output Stream
	 * @param eventID selected eventID
	 * @param boatID selected boatID
	 * @param CF owner CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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
			System.err.println("sendSubscription Check-Existence Error: " + e.getMessage());
		}
		disconnect();
	}

	/**
	 * This method is used on every login to sort out the winner of a competition that is being held the current day,
	 * it retrieve all the participants to a single competition and shuffles out a winner.
	 * 
	 * @param os Output Stream
	 * @param eventID current eventID
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */
	
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
	
	/**
	 * This method is used to modify the competition attribute podium to the newly extracted podium.
	 * 
	 * @param id competitionID
	 * @param podium new podium places
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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
	
	/**
	 * This method is used to selet a subscription of a precise boat from a precise competition, it can be done directly by the user logged in.
	 * 
	 * @param os Output Stream
	 * @param eventID passed event ID
	 * @param boatID passed boat ID
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

	public static void deleteSubscription(ObjectOutputStream os, Integer eventID, Integer boatID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		Statement statementI = connection.createStatement();
		try {
			ResultSet rs = statementI.executeQuery("SELECT * FROM Participants WHERE ID_Competition = " + eventID + " AND ID_Boat = \"" + boatID + "\";");
			while (rs.next() == true) {
				PreparedStatement stmtI = connection.prepareStatement("DELETE FROM Participants WHERE ID_Competition = " + eventID + " AND ID_Boat = " + boatID + ";");
				stmtI.executeUpdate();
				os.writeObject(new Message("OK"));
				os.flush();
				disconnect();
				return;
			}
			os.writeObject(new Message("NotSub"));
			os.flush();		
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existence Error: " + e.getMessage());
		}
		disconnect();
	}

	/**
	 * This method can be called only by the manager who wants to delete a precise competition.
	 * 
	 * @param os Output Stream
	 * @param eventID passed event ID
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

	public static void deleteEvent(ObjectOutputStream os, Integer eventID) throws ClassNotFoundException, SQLException, IOException {
		initializeConnection();
		try {
			PreparedStatement stmtI = connection.prepareStatement("DELETE FROM Competition WHERE ID = " + eventID + ";");
			stmtI.executeUpdate();
		} catch (Exception e) {
			System.err.println("sendSubscription Check-Existence Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * This method is called when a user wants to subscribe to a precise competition.
	 * 
	 * @param os Output Stream
	 * @param eventID passed event ID
	 * @param boatID passed boat ID
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */

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
	
	/**
	 * This method is called by the manager in order to add a new competition to the list and, then, allow other users to subscribe to it.
	 * 
	 * @param os Output Stream
	 * @param Name competition Name
	 * @param Prize competition Prize
	 * @param Cost competition Subscription Cost
	 * @param Date competition Date
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */
	
	public static void createEvent(ObjectOutputStream os, String Name, Double Prize, Double Cost, String Date) throws SQLException, ClassNotFoundException {
		initializeConnection();
		Statement statement = connection.createStatement();
		try {
			statement.executeUpdate("INSERT INTO Competition(Name, ID, Cost, Date, WinPrice, Podium) VALUES (\"" + Name + "\"," + null + "," + Cost + ",\"" + Date + "\",\"" + Prize + "\"," + 0 + ");");
		} catch (Exception e) {
			System.err.println("Insert sendSubscription Error: " + e.getMessage());
		}
		disconnect();
	}
	
	/**
	 * This method is called when a user wants to add a new boat, it checks if a boat with the same name is still into the database and
	 * returns a string (OK, KO) whether if it is found or not into the database.
	 * 
	 * @param os Output Stream
	 * @param CF owner CF
	 * @param boatName new boat Name
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 */

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

	/**
	 * This method is called when a user wants to delete a boat, it checks also if the selected boat is currently subscripted to a competition, and, if yes
	 * it shows an alert onto the user screen telling that, if he wants to delete that boat he will also unsubscribe from the subscripted competition.
	 * 
	 * @param os Output Stream
	 * @param ID boat ID
	 * @param Checker Controller
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws InterruptedException
	 */
	
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
	
	/**
	 * This method is used to retrieve the object Person passing the unique identifier CF.
	 * 
	 * @param os Output Stream
	 * @param CF unique user identifier CF
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 * @throws IOException Handles Input-Output Exceptions
	 * @throws InterruptedException
	 */

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
	
	/**
	 * This method is called whenever we wants to end the connection to the SQL Database.
	 * 
	 * @throws SQLException Handles SQL Errors
	 */

	public static void disconnect() throws SQLException {
		connection.close();
		return;
	}
	
	/**
	 * This method is the main one, which is firstly executed before all the others; it manages the Threads and the informations
	 * written onto the console.
	 * 
	 * @param args All the argument passed
	 * @throws SQLException Handles SQL Errors
	 * @throws ClassNotFoundException Handles The Non-Existence of A Class
	 */

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		LinkedList<ClientHandler> clients = new LinkedList<>();
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
