package clueless;

import org.json.JSONObject;


/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Location implements ClueInterface {

	public int id;
	public String type;
	public Location secretPassage = null;
	public int x_coordinate;
	public int y_coordinate;
	
	Location(int id, String type, int x, int y) {
		this.id = id;
		this.type = type;
		this.x_coordinate = x;
		this.y_coordinate = y;
	}
	
	/**
	 * Return true if location provided has same id
	 * @param l
	 * @return
	 */
	public boolean equals(Location l) {
		if (l.id == this.id) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if location provided his adjacent to this room
	 * @param l
	 * @return
	 */
	public boolean isAdjacent(Location l) {
		// TODO: determine if adjacent based on location coordinates
		return false;
	}
	
	/**
	 * Return true if location is a room
	 * @return
	 */
	public boolean isRoom() {
		if (this.type.equals(LOCATION_TYPE_ROOM)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if location is a hallway
	 * @return
	 */
	public boolean isHallway() {
		if (this.type.equals(LOCATION_TYPE_HALLWAY)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return true if location is a home
	 * @return
	 */
	public boolean isHome() {
		if (this.type.equals(LOCATION_TYPE_HOME)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns JSONObject representation for this location
	 */
	public JSONObject toJson() {
		JSONObject locationJson = new JSONObject();
		locationJson.put("id", this.id);
		locationJson.put("type", this.type);
		
		if (this.secretPassage == null) {
			locationJson.put("secretPassage", "null"); // just put ID of secret passage for JSON
		} else {
			locationJson.put("secretPassage", this.secretPassage.id); // just put ID of secret passage for JSON
		}
		locationJson.put("x_coordinate", this.x_coordinate);
		locationJson.put("y_coordinate", this.y_coordinate);
		
		return locationJson;
	}
}
