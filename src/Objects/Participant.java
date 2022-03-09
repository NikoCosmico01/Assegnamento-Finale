package Objects;

import java.io.Serializable;

/**
 * This object contains all the needed information in order to complete a competition or a boat subscription.
 * 
 * @author NicoT
 *
 */

public class Participant implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer eventID;
	private String eventName;
	private Double eventCost;
	private String eventDate;
	private Double eventPrize;
	private String eventPodium;

	private String boatName;
	private Integer boatID;

	/**
	 * Object Empty Constructor
	 */
	
	public Participant() {
		this.eventName = "";
		this.eventID = 0;
		this.eventCost = 0.0;
		this.eventDate = "";
		this.eventPrize = 0.0;
		this.eventPodium = "";
		this.boatName = "";
		this.boatID = 0;
	}

	/**
	 * Object Constructor
	 * 
	 * @param eventname Event Name
	 * @param eventid Event ID
	 * @param eventcost Event Sub Cost
	 * @param date Event Date
	 * @param prize Event Winning Prize 
	 * @param podium Event Podium
	 * @param boatname Boat Name
	 * @param boatid Boat ID
	 */
	public Participant(String eventname, Integer eventid, Double eventcost, String date, Double prize, String podium, String boatname, Integer boatid) {
		this.eventName = eventname;
		this.eventID = eventid;
		this.eventCost = eventcost;
		this.eventDate = date;
		this.eventPrize = prize;
		this.eventPodium = podium;
		this.boatName = boatname;
		this.boatID = boatid;
	}

	/**
	 * Object Constructor (Competition ONLY)
	 * 
	 * @param eventname Event Date
	 * @param ID Event ID
	 * @param eventcost Event Cost
	 * @param date Event Date
	 * @param prize Event Winning Prize
	 */
	public Participant(String eventname, Integer ID, Double eventcost, String date, Double prize) {
		this.eventName = eventname;
		this.eventCost = eventcost;
		this.eventID = ID;
		this.eventDate = date;
		this.eventPrize = prize;
	}
	
	/**
	 * GET Method 
	 * 
	 * @return Event Name
	 */
	public String getEventName() {
		return this.eventName;
	}
	/**
	 * GET Method 
	 * 
	 * @return Event ID
	 */
	public Integer getEventID() {
		return this.eventID;
	}
	/**
	 * GET Method 
	 * 
	 * @return Event Subscription Cost
	 */
	public Double getEventCost() {
		return this.eventCost;
	}
	/**
	 * GET Method 
	 * 
	 * @return Event Holding Date
	 */
	public String getEventDate() {
		return this.eventDate;
	}
	/**
	 * GET Method 
	 * 
	 * @return Event Winning Prize
	 */
	public Double getEventPrize() {
		return this.eventPrize;
	}
	/**
	 * GET Method (Calculates The Podium For Each Boat)
	 * 
	 * @return Event Podium
	 */
	public String getEventPodium() {
		String[] list = this.eventPodium.split("-");
		if (list.length == 0) {
			return null;
		}
		for (Integer i = 0; i < list.length; i++) {
			if (list[i].equals(String.valueOf(this.boatID))) {
				if (i+1 == 1) {
					return (i+1 + " Winner");
				} else {
					return String.valueOf(i+1);
				}
			}
		}
		return null;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat Name
	 */
	public String getBoatName() {
		return this.boatName;
	}
	/**
	 * GET Method 
	 * 
	 * @return Boat ID
	 */
	public Integer getBoatID() {
		return this.boatID;
	}

}
