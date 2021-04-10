package clueless;

import org.json.JSONObject;

/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Character implements ClueInterface {
	
	public String characterName;
	public Location characterHome;
	public Location currLocation;
	protected boolean wasMovedToRoom;
	protected boolean active;
	
	public Character(String charName, Location home) { // constructor solely used for ClueInterface (init map)
		this.characterName = charName;
		this.characterHome = home;
		this.currLocation = home; // always starts at home
		this.wasMovedToRoom = false; // always starts at home (not moved to room)
		this.active = false; // always inactive until player is created
	}

	/**
	 * Returns the wasMovedToRoom attribute for this character
	 * @return wasMovedToRoom
	 */
	public boolean getMovedToRoom() {
		return this.wasMovedToRoom;
	}

	/**
	 * Sets the wasMovedToRoom attribute for this character
	 * @param movedToRoom
	 */
	public void setMovedToRoom(boolean movedToRoom) {
		this.wasMovedToRoom = movedToRoom;
	}

	/**
	 * Returns the active state of the character
	 * @return active
	 */
	public boolean isActive() {
		return this.active;
	}
	
	/**
	 * Sets the character's active attribute to true
	 */
	public void activate() {
		this.active = true;
	}
	
	/**
	 * Sets the character's active attribute to false
	 */
	public void deactivate() {
		this.active = false;
	}
	
	/**
	 * Returns JSONObject representation for this character
	 */
	public JSONObject toJson() {
		JSONObject characterJson = new JSONObject();
//		characterJson.put("id", this.characterId);
		characterJson.put("characterName", this.characterName);
		characterJson.put("home", this.characterHome.toJson()); 
		characterJson.put("currLocation", this.currLocation.toJson()); 
		characterJson.put("wasMovedToRoom", this.wasMovedToRoom); 
		characterJson.put("active", this.active); 
		return characterJson;
	}
}
