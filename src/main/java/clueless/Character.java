package clueless;

import org.json.JSONObject;

/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Character implements ClueInterface {
	
	public int characterId;
	
	public String characterName;
	public Location characterHome;
	protected Location currLocation;
	protected boolean wasMovedToRoom;
	protected boolean active;
	
	public Character(int charId, String name, Location home) { // use this constructor for initCharacterMap() in ClueInterface
		this.characterId = charId;
		this.characterName = name;
		this.characterHome = home;
		this.currLocation = home; // always starts at home
		this.wasMovedToRoom = false; // always starts at home (not moved to room)
		this.active = false; // always inactive until player is created
	}
	
	public Character(int charId) {
		this.characterId = charId;
		this.characterName = initCharacterMap().get(charId).characterName;
		this.characterHome = initCharacterMap().get(charId).characterHome;
		this.currLocation = initCharacterMap().get(charId).characterHome; // always starts at home
		this.wasMovedToRoom = false; // always starts at home (not moved to room)
		this.active = false; // always inactive until player is created
	}

	/**
	 * Returns the current location for this character
	 * @return currLocation
	 */
	public Location getCurrLocation() {
		return this.currLocation;
	}

	/**
	 * Sets the current location for this character
	 * @param location
	 */
	public void setCurrLocation(Location location) {
		this.currLocation = location;
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
		characterJson.put("id", this.characterId);
		characterJson.put("characterName", this.characterName);
		characterJson.put("home", this.characterHome.toJson()); 
		characterJson.put("currLocation", this.currLocation.toJson()); 
		characterJson.put("wasMovedToRoom", this.wasMovedToRoom); 
		characterJson.put("active", this.active); 
		return characterJson;
	}
}
