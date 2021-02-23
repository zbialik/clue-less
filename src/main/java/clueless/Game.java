package clueless;

import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
/**
 * TODO: define game
 * 
 * @author zachbialik
 *
 */
class Game {

  private @Id @GeneratedValue Long id;
  private ArrayList<Player> players;

  Game() { // default constructor
	  this.players = new ArrayList<Player>();
  }

  Game(Player player) { // custom constructor
    this.players.add(player);
  }

  public Long getId() {
    return this.id;
  }

  public ArrayList<Player> getPlayers() {
    return this.players;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  @Override
  public String toString() {
    return "Game { \n\t" + "id=" + this.id + ", \n\tplayers= [" + this.players.toString() + "\n\t]\n}";
  }
}