package Objects;

import java.io.Serializable;

/**
 * Object used, sometimes, to send a message via an object.
 * 
 * @author NicoT
 *
 */

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String Msg;
	
	/**
	 * Object Empty Constructor
	 */
	
	public Message() {
		this.Msg = "";
	}
	
	/**
	 * SET Method
	 * 
	 * @param msg New Message
	 */
	public Message(String msg) {
		this.Msg = msg;
	}
	/**
	 * GET Method 
	 * 
	 * @return Message
	 */
	public String getMsg() {
		return this.Msg;
	}
}
