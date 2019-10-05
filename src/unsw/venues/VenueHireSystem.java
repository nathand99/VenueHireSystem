/**
 *
 */
package unsw.venues;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Venue Hire System for COMP2511.
 *
 * A basic prototype to serve as the "back-end" of a venue hire system. Input
 * and output is in JSON format.
 *
 * @author Robert Clifton-Everest
 *
 */
public class VenueHireSystem {
	List<Venue> venues;
	List<Booking> bookings;
    /**
     * Constructs a venue hire system. Initially, the system contains no venues,
     * rooms, or bookings.
     */
    public VenueHireSystem() {
        // TODO Auto-generated constructor stub
    	this.venues = new ArrayList<Venue>();
    	this.bookings = new ArrayList<Booking>();
    }

    private void processCommand(JSONObject json) {
        switch (json.getString("command")) {

        case "room":
            String venue = json.getString("venue");
            String room = json.getString("room");
            String size = json.getString("size");
            addRoom(venue, room, size);
            break;

        case "request":
            String id = json.getString("id");
            LocalDate start = LocalDate.parse(json.getString("start"));
            LocalDate end = LocalDate.parse(json.getString("end"));
            int small = json.getInt("small");
            int medium = json.getInt("medium");
            int large = json.getInt("large");

            JSONObject result = request(id, start, end, small, medium, large);

            System.out.println(result.toString(2));
            break;

        case "change":
        	
        case "cancel":
        	
        case "list":
        	
        }
    }

    private void addRoom(String venue, String room, String size) {
        // TODO Process the room command
    	// if this venue exists, then use it
    	Venue new_venue = null;
    	int length = 0;
    	if (!venues.isEmpty()) {
    	//if (venues != null) {
    		length = venues.size();
    	}
    	
    	for ( int i = 0; i < length ; i++ ) {
    		if (venues.get(i).name.equals(venue)) {
    			new_venue = venues.get(i);
    			break;
    		}
    	}
    	// if not, create a new venue
    	if (new_venue == null) {
    		//System.out.println("new venue made");
    		new_venue = new Venue(venue);
    	}   	
    	addVenue(new_venue);
    	// make the room and add it to venue
    	Room new_room = new Room(room, size);
    	new_venue.addRoom(new_room, size);
    	new_venue.toString();
    }

    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        JSONObject result = new JSONObject();

        // TODO Process the request commmand
    	
        // look through bookings:
        //	if (booking.room.size == small)
        // 
        for (Venue v : venues) {
        	// if venue doesnt have enough of requested rooms, goto next venue
        	if (v.small < small || v.medium < medium || v.large < large) {
        		continue;
        	}
        	if (canBook(v, start, end, v.small, v.medium, v.large)) {
        		// then book it
        		
        		// get first room -> loop through all bookings for the first room 
        		// use isBefore() and isAfter() with dates
        		// https://dzone.com/articles/working-with-localdate-localtime-and-localdatetime
        		// if can book then decrement amount of rooms needed
        		// if room booked out then
        	}
        }

        result.put("status", "success");
        result.put("venue", "Zoo");

        JSONArray rooms = new JSONArray();
        rooms.put("Penguin");
        rooms.put("Hippo");

        result.put("rooms", rooms);
        return result;
    }
    
    // adds venue to venues
    private void addVenue(Venue venue) {
    	venues.add(venue);
    }
    
    // looks at a venue and checks if booking can be made for that venue
    private Boolean canBook(Venue venue, LocalDate start, LocalDate end, int small, int medium, int large) {
    	int sr = small;
    	int mr = medium;
    	int lr = large;
    	for (Room r : venue.rooms) {
    		if (sr == 0) {
    			break;
    		}
    		if (r.size.equals("small")) {
    			if (isAvailable(r, start, end)) {
        			sr--;
        		}
    		}   		
    	}
    	for (Room r : venue.rooms) {
    		if (mr == 0) {
    			break;
    		}
    		if (r.size.equals("medium")) {
    			if (isAvailable(r, start, end)) {
        			mr--;
        		}
    		}   		
    	}
    	for (Room r : venue.rooms) {
    		if (lr == 0) {
    			break;
    		}
    		if (r.size.equals("large")) {
    			if (isAvailable(r, start, end)) {
        			lr--;
        		}
    		}   		
    	}
    	
    	if (sr == 0 && mr == 0 && lr == 0) {
    		return true;
    	} else {
    		return false;
    	}  	
    }
    
    private Boolean isAvailable(Room r, LocalDate start, LocalDate end) {
    	return true;
    }
    
    public static void main(String[] args) {
        VenueHireSystem system = new VenueHireSystem();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.trim().equals("")) {
                JSONObject command = new JSONObject(line);
                system.processCommand(command);
            }
        }
        sc.close();
    }

}
