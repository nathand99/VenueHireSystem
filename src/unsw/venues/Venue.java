package unsw.venues;

import java.util.ArrayList;
import java.util.List;

public class Venue {
	String name;
	List<Room> rooms = new ArrayList<Room>();
	
	/**
	 * constructor
	 * @param size
	 */
	public Venue(String name) {
		super();
		this.name = name;
	}
	
	public void addRoom(Room new_room) {
		rooms.add(new_room);
	}

	@Override
	public String toString() {
		int length = rooms.size();
		for ( int i = 0; i < length ; i++ ) {
    		System.out.println(rooms.get(i));
		}
		return "";
	}
	
}
