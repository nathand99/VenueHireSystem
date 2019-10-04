package unsw.venues;

public class Room {
	String name;
	String size;

	public Room(String name, String size) {
		super();
		this.name = name;
		this.size = size;
	}

	@Override
	public String toString() {
		String message = "name: " + name + " size: " + size;
		return message;
	}
	
}
