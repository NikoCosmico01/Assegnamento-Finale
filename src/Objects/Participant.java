package Objects;

import java.io.Serializable;

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

	public String getEventName() {
		return this.eventName;
	}

	public Integer getEventID() {
		return this.eventID;
	}

	public Double getEventCost() {
		return this.eventCost;
	}

	public String getEventDate() {
		return this.eventDate;
	}

	public Double getEventPrize() {
		return this.eventPrize;
	}

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

	public String getBoatName() {
		return this.boatName;
	}

	public Integer getBoatID() {
		return this.boatID;
	}

}
