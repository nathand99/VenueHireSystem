package unsw.venues;

import java.util.ArrayList;
import java.util.List;

public class Venue {
	String name;
	List<Room> rooms = new ArrayList<Room>();
	int small = 0;
	int medium = 0;
	int large = 0;
		
	
	/**
	 * constructor
	 * @param size
	 */
	public Venue(String name) {
		super();
		this.name = name;
	}
	
	public void addRoom(Room new_room, String size) {
		rooms.add(new_room);
		switch (size) {
        case "small":
        	this.small++;
        case "medium":
        	this.medium++;
        case "large":
        	this.large++;
		}
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
