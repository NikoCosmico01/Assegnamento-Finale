package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import Person.Partner;

public class Link {

	public static Partner checkLogin(String userName, String passWord) throws SQLException {
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sailingclub?user=root&password=");
		Statement stmt = cn.createStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM Partner;");
			while (rs.next() == true) {
				if (userName.equals(rs.getString("UserName")) && passWord.equals(rs.getString("PassWord"))) {
					System.out.println("Login Succeded");
					return new Partner(rs.getString("Name"),rs.getString("Surname"),rs.getString("Address"),rs.getString("CF"),rs.getInt("ID_Club"), userName, passWord);
				}
			}
			System.out.println("Login Non Avvenuto");
		} catch (SQLException e) {
			System.out.println("Errore:" + e.getMessage());
		}
		cn.close();
		return null;
	}
}
