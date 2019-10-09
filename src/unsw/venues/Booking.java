package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Booking {
	String id;
	Venue venue;
	List<Room> rooms = new ArrayList<Room>();
	LocalDate start_date;
	LocalDate end_date;
	
	/**
	 * Constructor (no rooms)
	 * @param id
	 * @param venue
	 * @param start_date
	 * @param end_date
	 * 
	 * returns a booking
	 */
	public Booking(String id, Venue venue, LocalDate start_date, LocalDate end_date) {
		super();
		this.id = id;
		this.venue = venue;
		this.start_date = start_date;
		this.end_date = end_date;
	}
	
	public void addRoom(Room r) {
		this.rooms.add(r);
	}
}
