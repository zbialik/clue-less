package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
/**
 * TODO: check definition
 * The Game class represents a temporary game being held for a group
 * of players. Each Game instantation has a unique id, a set of players,
 * and map of the board (ClueMap object).
 * 
 * @author Zach Bialik
 */
public class Game implements ClueInterface {
	private static final Logger LOGGER = LogManager.getLogger(Game.class);
	
	@Id
	public int gameId; // game unique ID
	private boolean hasStarted;
	private List<Card> mysteryCards;
	private List<Player> players; // list of active players in the game
	private Map<Integer, Character> characterMap = new HashMap<Integer, Character>();
	private String eventMessage;

	public Game(int id) { // custom constructor
		this.gameId = id;
		this.mysteryCards = new ArrayList<Card>();
		this.hasStarted = false;
		this.players = new ArrayList<Player>();
		this.characterMap = CHARACTER_ID_MAP;
		this.eventMessage = new String();
	}
	
	/**
	 * Starts the game by changing hasStarted to true
	 */
	public void startGame() {
		this.hasStarted = true;
	}
	
	/**
	 * Changes turns for the game
	 */
	public void changeTurn() {
		// TODO: complete logic
	}
	
	/**
	 * Returns the next player whose turn it is in the game
	 * @return
	 */
	public Player nextPlayer() {
		Player pl = null;
		
		// TODO: complete logic
		
		return pl;
	}
	
	/**
	 * Helper method for determining if a location is occupied in this game
	 */
	public boolean isLocationOccupied() {
		boolean occupied = false;

		// TODO: complete logic
		
		return occupied;
	}
	
	/**
	 * Helper method for determining if a location is occupied in this game
	 */
	public void updatePossibleMoves(Character c) {
		// TODO: complete logic
	}

	/**
	 * Adds a player to the game
	 * @param newPlayer
	 */
	public Game setPlayerToCharacter(Player newPlayer) {
		
		// TODO: verify symbolic linkage works!
		
		// add new player to players list
		this.players.add(newPlayer); 
		
		// update character map with new player
		this.characterMap.put(newPlayer.characterId, this.players.get(-1)); 
		
		return this;
	}
	
	/**
	 * Helper method for determining if accusation provided is correct
	 * @param accusation
	 * @return correct
	 */
	public boolean isAccusationCorrect(List<Card> accusation) {
		boolean correct = false;

		// TODO: complete logic
		
		return correct;
	}
	
	/**
	 * Returns the player provided their characterId
	 * @param characterId
	 * @return
	 */
	public Character getCharacter(int characterId) {
		
		try {
			return this.characterMap.get(characterId);
		} catch (Exception e) {
			throw new PlayerNotFoundException(characterId); // if not found, throw exception
		}
		
	}

	/**
	 * Returns the player provided their characterId
	 * @param characterId
	 * @return
	 */
	public Player getPlayer(int characterId) {
		
		try {
			Character character = this.characterMap.get(characterId);
			if (character instanceof Player) {
				return (Player) character;
			} else {
				LOGGER.error("getPlayer( " + characterId +  " ) could not cast to Character to Player");
				return null;
			}
		} catch (Exception e) {
			throw new PlayerNotFoundException(characterId); // if not found, throw exception
		}
	}
	
	/**
	 * Returns the next player that has a clue for the suggestion. If no one, then returns null
	 * @param suggestion
	 * @return
	 */
	public Player whoHasClue(List<Card> suggestion) {
		Player pl = null;
		
		// TODO: complete logic
		
		return pl;
	}
	
	/**
	 * Returns JSONObject representation for this game
	 */
	public JSONObject toJson() {
		
		JSONObject gameJson = new JSONObject();
		gameJson.put("gameId", this.gameId);
		gameJson.put("hasStarted", this.gameId);
		gameJson.put("players", playersToJsonArray(this.players));
		gameJson.put("eventMessage", this.eventMessage); 
		gameJson.put("characterMap", charMapToJsonObject(this.characterMap));
		
		return gameJson;
	}
	
//	TODO: delete if OBE
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (!(o instanceof Game))
//			return false;
//		Game gameO = (Game) o;
//		return Objects.equals(this.gameId, gameO.gameId) && Objects.equals(this.players, gameO.players);
//	}
}