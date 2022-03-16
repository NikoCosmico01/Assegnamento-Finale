package Main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import Socket.Server;

/**
 * JUnit Test Cases
 * 
 * @author NicoT
 *
 */

class JUnitTest {
	
	/**
	 * Event Existence Check
	 * 
	 * @throws ClassNotFoundException Handles Non Existing Class
	 * @throws SQLException Handles MySQL Exception
	 * @throws IOException Handles Input/Output Exception
	 */
	@Test
	void testEvent() throws ClassNotFoundException, SQLException, IOException {		
		String resultString = Server.checkEvent(null, 30, 101, "ILNTRZZ");
		assertEquals("OK", resultString);
	}
	
	/**
	 * User Credentials Check
	 * 
	 * @throws ClassNotFoundException Handles Non Existing Class
	 * @throws SQLException Handles MySQL Exception
	 * @throws IOException Handles Input/Output Exception
	 */
	@Test
	void testLogin() throws ClassNotFoundException, SQLException, IOException {		
		String resultString = Server.checkLogin(null, "Ile", "Prova");
		assertEquals("OK", resultString);
	}
	
	/**
	 * User Existence Check
	 * 
	 * @throws ClassNotFoundException Handles Non Existing Class
	 * @throws SQLException Handles MySQL Exception
	 * @throws IOException Handles Input/Output Exception
	 */
	@Test
	void testUserExistance() throws ClassNotFoundException, SQLException, IOException {		
		String resultString = Server.checkUserExistence(null, "Andre");
		assertEquals("OK", resultString);
	}

}
