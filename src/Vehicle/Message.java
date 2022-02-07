package Vehicle;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String Msg;
	
	public Message() {
		this.Msg = "";
	}
	
	public Message(String msg) {
		this.Msg = msg;
	}
	
	public String getMsg() {
		return this.Msg;
	}
}
