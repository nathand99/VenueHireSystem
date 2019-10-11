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
        	String change_id = json.getString("id");
            LocalDate change_start = LocalDate.parse(json.getString("start"));
            LocalDate change_end = LocalDate.parse(json.getString("end"));
            int change_small = json.getInt("small");
            int change_medium = json.getInt("medium");
            int change_large = json.getInt("large");
        	// delete old booking
            cancel(change_id);
        	// make new booking with same id
            JSONObject change_result = request(change_id, change_start, change_end, change_small, change_medium, change_large);
            System.out.println(change_result.toString(2));
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
    
    /**
     * Method - adds a room to a venue. If venue doesn't exist, it is created
     * @param venue - venue for room to be added to
     * @param room - name of room to be added
     * @param size - room size (small, medium, or large)
     */
    private void addRoom(String venue, String room, String size) {
    	// if this venue exists, then use it
    	Venue new_venue = null;
    	int length = 0;
    	if (!venues.isEmpty()) {
    	//if (venues != null) {
    		length = venues.size();
    		for ( int i = 0; i < length ; i++ ) {
        		if (venues.get(i).name.equals(venue)) {
        			new_venue = venues.get(i);
        			break;
        		}
        	}
    	}	   	
    	// if venue doesn't exist, create a new venue
    	if (new_venue == null) {
    		//System.out.println("new venue made");
    		new_venue = new Venue(venue);
    		addVenue(new_venue);
    	}   		
    	// make the room and add it to venue
    	Room new_room = new Room(room, size);
    	new_venue.addRoom(new_room, size);
    	//new_venue.toString();
    }

    public JSONObject request(String id, LocalDate start, LocalDate end,
            int small, int medium, int large) {
        JSONObject result = new JSONObject();
    	
        // look through venues
        for (Venue v : venues) {        	
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
            	        addBooking(new_booking);
            	        System.out.println("****** PRINT ALL BOOKINGS *******");
            	        printBookings();
            	        return result;
            		}
            	}
        	}
        }
        result.put("status", "rejected");    
	    return result;    
    }
    
    public void cancel(String id) {
    	Booking to_remove = null;
    	for (Booking b : bookings) {
			if (b.id.equals(id)) {
				to_remove = b;
			}
    	}
    	bookings.remove(to_remove);
    }
    
    // Output a list of the occupancy for all rooms at Zoo, in order of room declarations, then date
    public void list(String venue_string) {
    	Venue venue = null;
    	for (Venue v : venues) {
    		if (v.name.equals(venue_string)) {
    			venue = v;
    			break;
    		}
    	}
    	// for each room in the venue
    	for (Room r : venue.rooms) {    		
	    	for (Booking b : bookings) {
				if (b.rooms.contains(r)) {
					// [ { "room": "Penguin", "reservations": [
				    // { "id": "CSE Autumn Ball", "start": "2019-03-25", "end": "2019-03-26" },
				    // { "id": "Annual Meeting", "start": "2019-03-27", "end": "2019-03-29" }
				    // ] },
					String id = b.id;
					LocalDate start = b.start_date;
					LocalDate end = b.end_date;
					// print all the stuff
					
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
    	// if venue doesnt have enough of requested rooms - cannot book
    	if (venue.small < small || venue.medium < medium || venue.large < large) {
    		return false;
    	}
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
    			if (isAvailable(r, start, end)) {
        			sr--;
        		}
    		} else if (r.size.equals("medium") && mr > 0) {
    			if (isAvailable(r, start, end)) {
        			mr--;
        		}
    		} else if (r.size.equals("large") && lr > 0) {
    			if (isAvailable(r, start, end)) {
        			lr--;
        		}
    		}   
    	}
    	// all rooms booked - so can book
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
    		System.out.print("booking $$$$$$$$$$$$$$$$$$$$$$$");
    		b.toString();
    		
    		if (b.rooms.contains(r)) {
    			System.out.println("BOOKING CONTAINS R");
    			// if start equals the booking start or end date return false
    			if (start.equals(b.start_date) || start.equals(b.end_date)) {
    				System.out.println("CANNOT BOOK");
    				return false;
				// if end equals the booking start or end date return false
    			} else if (end.equals(b.start_date) || end.equals(b.end_date)) {
    				System.out.println("CANNOT BOOK");
    				return false;
    			}
    			// if start and end are inside the start_date and end_date of a booking return false
    			else if (start.isAfter(b.start_date) && end.isBefore(b.end_date)) { 	
    				System.out.println("CANNOT BOOK");
    				return false; 			
    			}
    			// if start is before booking start_date and end is after booking end_date return false
    			else if (start.isBefore(b.start_date) && end.isAfter(b.end_date)) { 	
    				System.out.println("CANNOT BOOK");
    				return false; 			
    			}
    			// if start is before booking start_date and end is after booking start_date return false
    			else if (start.isBefore(b.start_date) && end.isAfter(b.start_date)) { 	
    				System.out.println("CANNOT BOOK");
    				return false; 			
    			}
    			// if start is before booking end_date and end is after booking end_date return false
    			else if (start.isBefore(b.end_date) && end.isAfter(b.end_date)) { 	
    				System.out.println("CANNOT BOOK");
    				return false; 			
    			}
    		}
    	}
    	System.out.print("I CAN BOOK");
    	return true;
    }
    
    public void addBooking(Booking b) {
    	bookings.add(b);
    }
    
    public void printBookings() {
    	for (Booking b : bookings) {
    		System.out.println("##################");
    		b.toString();
    		System.out.println("##################");
    	}
    }
    
    public void printVenues() {
		for (Venue v : venues) {
			System.out.println("^^^^^^^^^^^^");
			v.toString();
		}
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
