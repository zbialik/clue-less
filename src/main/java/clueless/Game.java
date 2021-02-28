package clueless;

import java.util.HashMap;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
/**
 * The Game class represents a temporary game being held for a group
 * of players. Each Game instantation has a unique id, a set of players,
 * and map of the board (ClueMap object).
 * 
 * @author Zach Bialik
 */
public class Game {
	
	private static final Logger LOGGER = LogManager.getLogger(Game.class); // use logger as needed
	
	@Id
	private int gameId; // game unique ID
	
	private int playerId = 1; // will use to set player ID's
	private HashMap<Integer, Player> players; // map of players based on player ID
	//  private ClueMap currMap; // current map of the game board.

	public Game(int id) { // custom constructor
		this.gameId = id;
		this.players = new HashMap<Integer, Player>();
		// this.currMap = new ClueMap(); TODO update properly
	}

	/**
	 * Returns get ID of the game
	 * @return
	 */
	public int getId() {
		return this.gameId;
	}

	/**
	 * Adds a player to the game
	 * @param newPlayer
	 */
	public Game addPlayer(Player newPlayer) {
		
		newPlayer.setId(this.playerId); // set newPlayer's ID
		
		this.players.put(this.playerId, newPlayer); // add new player to this game
		
		this.playerId++; // increment player ID for the next time we add a player
		
		return this;
	}
	
	/**
	 * Update player in the game
	 * @param updatedPlayer
	 */
	public Game updatePlayer(int pid, Player updatedPlayer) {
		updatedPlayer.setId(this.players.get(pid).getId()); // ensure updatedPlayer has correct playerId before replacing
		this.players.put(pid, updatedPlayer);
		return this;
	}

	/**
	 * Returns the player provided their pid
	 * @return
	 */
	public Player getPlayer(int pid) {
		
		try {
			return this.players.get(pid);
		} catch (Exception e) {
			throw new PlayerNotFoundException(pid); // if not found, throw exception
		}
		
	}

	/**
	 * Returns the list of players in the game
	 * @return
	 */
	public HashMap<Integer, Player> getPlayers() {
		return this.players;
	}

	/**
	 * Sets the ID for the game
	 * @param id
	 */
	public void setId(int id) {
		this.gameId = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Game))
			return false;
		Game gameO = (Game) o;
		return Objects.equals(this.gameId, gameO.gameId) && Objects.equals(this.players, gameO.players);
	}

	/**
	 * Returns information relevant to the game
	 */
	@Override
	public String toString() {
		return this.toJson().toString(4) + '\n'; // apply pretty formatting
	}
	
	/**
	 * Returns information relevant to the game
	 */
	public JSONObject toJson() {
		
		JSONObject gameInfoJson = new JSONObject();
		gameInfoJson.put("gameId",this.gameId);
		
		JSONArray playersJson = new JSONArray();
		for (Player player : this.players.values()) {
			playersJson.put(player.toJson());
		}
		
		gameInfoJson.put("players", playersJson);
		
		return gameInfoJson; // apply pretty formatting
	}
}