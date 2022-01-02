package Event;

import java.io.Serializable;

public class Participants implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer eventID;
	private String eventName;
	private Double eventCost;
	private String boatName;
	private Integer boatID;
	
	public Participants() {
		this.eventName = "";
		this.eventID = 0;
		this.eventCost = 0.0;
		this.boatName = "";
		this.boatID = 0;
	}
	
	public Participants(String eventname, Integer eventid, Double eventcost, String boatname, Integer boatid) {
		this.eventName = eventname;
		this.eventID = eventid;
		this.eventCost = eventcost;
		this.boatName = boatname;
		this.boatID = boatid;
	}
	
	public String getEventName() {
		return this.eventName;
	}
	
	public Integer getEventID() {
		return this.eventID;
	}
	
	public Double getEventCost() {
		return this.eventCost;
	}
	
	public String getBoatName() {
		return this.boatName;
	}
	
	public Integer getBoatID() {
		return this.boatID;
	}
	
}
