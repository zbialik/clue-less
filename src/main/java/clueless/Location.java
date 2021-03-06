package clueless;

import java.util.Objects;

import org.json.JSONObject;


/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Location implements ClueInterface {

	public String name;
	public String type;
	public Location secretPassage = null;
	public int x_coordinate;
	public int y_coordinate;
	
	Location(String name, String type, int x, int y) {
		this.name = name;
		this.type = type;
		this.x_coordinate = x;
		this.y_coordinate = y;
	}
	
	/**
	 * Return true if location provided has same id
	 * @param l
	 * @return
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Location)) {
			return false;
		} else {
			Location locO = (Location) o;
			return Objects.equals(this.name, locO.name) 
					&& Objects.equals(this.type, locO.type);
		}
	}
	
	/**
	 * Return true if location provided his adjacent to this room
	 * @param loc
	 * @return isAdjacent
	 */
	public boolean isAdjacent(Location loc) {
		
		if ((this.x_coordinate == loc.x_coordinate) && (Math.abs(this.y_coordinate - loc.y_coordinate) == 1)) {
			return true;
		} else if ((this.y_coordinate == loc.y_coordinate) && (Math.abs(this.x_coordinate - loc.x_coordinate) == 1)) {
			return true;
		} else {
			return false;
		}
		
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
	 * Return true if location has a secretPassage
	 * @return
	 */
	public boolean hasSecretPassage() {
		if (Objects.isNull(this.secretPassage)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns JSONObject representation for this location
	 */
	public JSONObject toJson() {
		JSONObject locationJson = new JSONObject();
		locationJson.put("name", this.name);
		locationJson.put("type", this.type);
		
		if (!this.hasSecretPassage()) {
			locationJson.put("secretPassage", new JSONObject()); // just put ID of secret passage for JSON
		} else {
			locationJson.put("secretPassage", this.secretPassage.toJsonSecretPassage()); // put in JSON of location for secret passage
		}
		locationJson.put("x_coordinate", this.x_coordinate);
		locationJson.put("y_coordinate", this.y_coordinate);
		
		return locationJson;
	}
	
	/**
	 * Returns JSONObject representation for this secret passage
	 */
	public JSONObject toJsonSecretPassage() {
		JSONObject locationJson = new JSONObject();
		locationJson.put("name", this.name);
		locationJson.put("type", this.type);
		locationJson.put("x_coordinate", this.x_coordinate);
		locationJson.put("y_coordinate", this.y_coordinate);
		
		return locationJson;
	}
}
