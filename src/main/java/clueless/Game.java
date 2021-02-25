package clueless;

import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
/**
 * The Game class represents a temporary game being held for a group
 * of players. Each Game instantation has a unique id, a set of players,
 * and map of the board (ClueMap object).
 * 
 * @author Zach Bialik
 */
public class Game {
	
  private @Id @GeneratedValue Long id; // game's unique ID
  private ArrayList<Player> players; // list of players
//  private ClueMap currMap; // current map of the game board.

  Game() { // default constructor
	  this.players = new ArrayList<Player>();
		// this.currMap = new ClueMap(); TODO update properly
  }

  Game(Player player) { // custom constructor
    this.players.add(player);
	// this.currMap = new ClueMap(); TODO update properly
  }

  /**
   * Returns get ID of the game
   * @return
   */
  public Long getId() {
    return this.id;
  }
  
  /**
   * Adds a player to the game
   * @param newPlayer
   */
  public void addPlayer(Player newPlayer) {
	  this.players.add(newPlayer);
  }
  
  /**
   * Returns the list of players in the game
   * @return
   */
  public ArrayList<Player> getPlayers() {
    return this.players;
  }
  
  /**
   * Sets the ID for the game
   * @param id
   */
  public void setId(Long id) {
    this.id = id;
  }
  
  /**
   * Sets the players for the game
   * @param players
   */
  public void setPlayers(ArrayList<Player> players) {
    this.players = players;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Game))
      return false;
    Game gameO = (Game) o;
    return Objects.equals(this.id, gameO.id) && Objects.equals(this.players, gameO.players);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  /**
   * Returns information relevant to the game
   */
  @Override
  public String toString() {
    return "Game: \n{ \n\t" + "id=" + this.id + ", \n\tplayers= " + this.players.toString() + "\n}";
  }
}