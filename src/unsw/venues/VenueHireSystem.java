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
        	// delete old booking
        	// make new booking with same id
        	break;
        case "cancel":
        	String id_cancel = json.getString("id");
        	cancel(id_cancel);
        	break;
        case "list":
        	String venue_list = json.getString("venue");
        	list(venue_list);
        	break;    	
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
    	
        // look through venues
        int i = 0;
        for (Venue v : venues) {
        	// if venue doesnt have enough of requested rooms, goto next venue
        	if (v.small < small || v.medium < medium || v.large < large) {
        		continue;
        	}
        	System.out.println(i);
        	if (canBook(v, start, end, small, medium, large)) {
        		System.out.println("I can book");
        		// then book it
        		Booking new_booking = new Booking(id, v, start, end);
        		// small, medium, large rooms required - decremented as they are booked
        		int sr = small;
            	int mr = medium;
            	int lr = large;
            	JSONArray rooms = new JSONArray();
            	for (Room r : v.rooms) {
            		// all booking requests fulfilled
            		
            		if (r.size.equals("small") && sr > 0) {
            			if (isAvailable(r, start, end)) {
            				new_booking.addRoom(r);
            				rooms.put(r);
                			sr--;
                		}
            		}
            		if (r.size.equals("medium") && mr > 0) {
            			if (isAvailable(r, start, end)) {
            				new_booking.addRoom(r);
            				rooms.put(r);
                			mr--;
                		}
            		}   
            		if (r.size.equals("large") && lr > 0) {
            			if (isAvailable(r, start, end)) {
            				new_booking.addRoom(r);
            				rooms.put(r);
                			lr--;
                		}
            		}
            		if (sr == 0 && mr == 0 && lr == 0) {
            			result.put("status", "success");
            	        result.put("venue", v.name);
            	        result.put("rooms", rooms);
            	        return result;
            		}
            	}
        	}
        }
        result.put("status", "rejected");    
	    return result;    
    }
    
    public void cancel(String id) {
    	for (Booking b : bookings) {
			if (b.id.equals(id)) {
				bookings.remove(b);
			}
    	}
    }
    
    // Output a list of the occupancy for all rooms at Zoo, in order of room declarations, then date
    public void list(Venue venue) {
    	// for each room in the venue
    	for (Room r : venue.rooms) {
    		
	    	for (Booking b : bookings) {
				if (b.rooms.contains(r.name)) {
					bookings.remove(b);
				}
	    	}
    	}
    }
    
    // adds venue to venues
    private void addVenue(Venue venue) {
    	venues.add(venue);
    }
    
    // looks at a venue and checks if booking can be made for that venue
    private Boolean canBook(Venue venue, LocalDate start, LocalDate end, int small, int medium, int large) {
    	// number of small, medium, large rooms required. Decremented as room can be booked
    	int sr = small;
    	int mr = medium;
    	int lr = large;
    	// for each room in the venue - if size needed, checks if room is available
    	for (Room r : venue.rooms) {
    		// all rooms are booked - so can book
    		if (sr == 0 && mr == 0 && lr == 0) {
    			return true;
    		}
    		if (r.size.equals("small") && sr > 0) {
    			System.out.println("small room");
    			if (isAvailable(r, start, end)) {
        			sr--;
        		}
    		}
    		if (r.size.equals("medium") && mr > 0) {
    			if (isAvailable(r, start, end)) {
        			mr--;
        		}
    		}   
    		if (r.size.equals("large") && lr > 0) {
    			if (isAvailable(r, start, end)) {
        			lr--;
        		}
    		}   
    	}
    	// all rooms booked - so can book
    	System.out.println(sr);
    	System.out.println(mr);
    	System.out.println(lr);
    	if (sr == 0 && mr == 0 && lr == 0) {
			return true;
		// not all bookings satisfied - cannot book
		} else {
			return false;
		}
    	
    	
    }
    
    // check if a room is available by going through all bookings for that room
    private Boolean isAvailable(Room r, LocalDate start, LocalDate end) {
    	for (Booking b : bookings) {
    		if (b.rooms.contains(r)) {
    			// if start and end are not both before or both after a booking, then return false
    			if (!((start.isBefore(b.start_date) && end.isBefore(b.end_date)) || !((start.isAfter(b.start_date) && end.isAfter(b.end_date))))) {
    				return false;
    			}   			
    		}
    	}
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
