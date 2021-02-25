package clueless;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Player {
	
	private String name;
	private @Id @GeneratedValue Long id; // game's unique ID
	private boolean isTurn;

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
}
