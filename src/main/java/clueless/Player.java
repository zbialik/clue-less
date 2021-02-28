package clueless;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
/**
 * The Player class represents a unique player in a given player of clue-less.
 * 
 * @author Zach Bialik
 */
public class Player implements Serializable {

	public String name;
	private @Id @GeneratedValue Long id; // player's unique ID
	public boolean isTurn;
	private Long gameId; // player's associated gameId
	
	// TODO: use boardLocation or remove
//	public int[] boardLocation; // current location of player on the clue board

	Player() { // default constructor
		this.setName("NO NAME");
		this.isTurn = false;
		this.setGameId((long) 0);
	}

	Player(String name, Long gameId) { // unqique constructor
		this.setName(name);
		this.isTurn = false;
		this.setGameId(gameId);
	}

	/**
	 * Returns get ID of the player
	 * @return
	 */
	public Long getId() {
		return this.id;
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
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
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
		return Objects.equals(this.id, playerO.id) && Objects.equals(this.name, playerO.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}

	/**
	 * Returns information relevant to the player
	 */
	@Override
	public String toString() {
		return "{ Player ID: " + this.id + ", Player Name: " + this.name + ", Game ID: " + this.gameId + "}";
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}
	
}
