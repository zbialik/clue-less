package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

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

	public int gameId; // game unique ID

	public boolean hasStarted;
	public List<Card> mysteryCards;
	public Map<String, Character> characterMap = new HashMap<String, Character>();

	public String eventMessage; 

	public Game(int id) { // custom constructor
		this.gameId = id;
		this.mysteryCards = new ArrayList<Card>();
		this.hasStarted = false;
		this.eventMessage = new String();

		// copy character map from interface to this game
		this.characterMap.putAll(INIT_CHARACTER_MAP);
	}

	/**
	 * Starts the game by dealing cards to players
	 * and changing hasStarted to true
	 */
	public void startGame() {

		// TODO: deal player's cards
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
	 * Adds/sets a player to a character in the game. This method is synchronized to avoid two
	 * players being added at same time and one overwriting the other.
	 * @param newPlayer
	 */
	public synchronized Game addPlayer(String charName, String name) {
		Player newPlayer;
		
		if (this.getActivePlayers().isEmpty()) { // this is VIP player
			newPlayer = new Player(charName, name, true);
		} else {
			newPlayer = new Player(charName, name);
		}
		// update character map with new player
		this.characterMap.put(newPlayer.characterName, newPlayer);
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
	public Character getCharacter(String charName) {

		try {
			return this.characterMap.get(charName);
		} catch (Exception e) {
			throw new PlayerNotFoundException(charName); // if not found, throw exception
		}

	}

	/**
	 * Returns the player provided their characterId
	 * @param characterId
	 * @return
	 */
	public Player getPlayer(String charName) {

		try {
			Character character = this.characterMap.get(charName);
			if (character instanceof Player) {
				return (Player) character;
			} else {
				LOGGER.error("getPlayer( " + charName +  " ) could not cast to Character to Player");
				return null;
			}
		} catch (Exception e) {
			throw new PlayerNotFoundException(charName); // if not found, throw exception
		}
	}

	/**
	 * Returns a list of active Players in the game
	 * @param characterId
	 * @return
	 */
	public List<Player> getActivePlayers() {

		List<Player> playersList = new ArrayList<Player>();

		// TODO: test this logic

		for ( Character character : characterMap.values() ) {
			if (character instanceof Player) {
				if ( ((Player) character).isActive() ) {
					playersList.add((Player) character);
				}
			}
		}

		return playersList;

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
		gameJson.put("hasStarted", this.hasStarted);
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