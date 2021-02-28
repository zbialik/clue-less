package clueless;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONObject;

@SuppressWarnings("serial")
@Entity
/**
 * The Player class represents a unique player in a given player of clue-less.
 * 
 * @author Zach Bialik
 */
public class Player implements Serializable {

	@Id 
	private int playerId; // player's unique ID

	public String name;
	public boolean isTurn;

	// TODO: use boardLocation or remove
	//	public int[] boardLocation; // current location of player on the clue board

	public Player() { // default constructor
		this.setName("NO NAME");
		this.isTurn = false;
	}

	public Player(String name) { // unqique constructor
		this.setName(name);
		this.isTurn = false;
	}

	/**
	 * Returns get ID of the player
	 * @return
	 */
	public int getId() {
		return this.playerId;
	}

	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the ID for the player
	 * @param playerId
	 */
	public void setId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Sets the name for the player
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Player))
			return false;
		Player playerO = (Player) o;
		return Objects.equals(this.playerId, playerO.playerId) && Objects.equals(this.name, playerO.name);
	}

	/**
	 * Returns information relevant to the player
	 */
	@Override
	public String toString() {
		return this.toJson().toString(4) + '\n'; // applly pretty formatting
	}
	
	/**
	 * Returns information relevant to the player
	 */
	public JSONObject toJson() {
		JSONObject playerJson = new JSONObject();
		playerJson.put("playerId", this.playerId);
		playerJson.put("name", this.name);
		playerJson.put("isTurn", this.isTurn);
		return playerJson;
	}
	
}
