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

		// deal player's cards
		dealCards(this);
		
		// set the starting player's turn
		this.startingPlayer().state = PLAYER_STATE_WAIT;
		
		this.hasStarted = true;
	}

	/**
	 * Changes turns for the game
	 */
	public void changeTurn() {
		// TODO: (MEGAN) complete logic
		
		// NOTE: determine the next player using the this.nextPlayer()
	}

	/**
	 * Returns the next player whose turn it is in the game
	 * @return nextPlayer
	 */
	public Player nextPlayer() {

		// TODO: (MEGAN) complete logic
		
		/*
		 * Normal Workflow:
		 * 	1. find the Player in this.characterMap that does NOT have a 'wait' state
		 * 			- you will want to use ' instanceof Player' logic to skip characters 
		 * 			  that aren't Players
		 * 			- you may assume only one character is NOT in 'wait' state when this method is called 
		 * 			  during normal game runtime
		 * 
		 * 	2. determine what index this Player is at using the constant ordered array CHARACTER_TURN_ORDER (from ClueInterface)
		 * 	3. grab the next player's name from CHARACTER_TURN_ORDER
		 * 	4. use the grabbed player's name to return this.characterMap.get(<the player's name you grabbed>)
		 * 
		 * NOTE: you can see startingPlayer() for some guidance on looping over the ordered CHARACTER_TURN_ORDER array
		 */

		return null;
	}
	
	/**
	 * Returns the player whose turn it is when starting the game
	 * @return startingPlayer
	 */
	public Player startingPlayer() {
		
		for (int i = 0; i < CHARACTER_TURN_ORDER.length; i++) {
			if (this.characterMap.get(CHARACTER_TURN_ORDER[i]) instanceof Player) {
				return ((Player) this.characterMap.get(CHARACTER_TURN_ORDER[i]));
			}
		}
		
		return null; // return null if couldn't find a player
	}

	/**
	 * Helper method for determining if a location is occupied in this game
	 */
	public boolean isLocationOccupied() {
		boolean occupied = false;

		// TODO: (ALEX) complete logic

		return occupied;
	}

	/**
	 * Helper method for determining if a location is occupied in this game
	 */
	public void updatePossibleMoves(Character c) {
		// TODO: (ZACH) complete logic
	}

	/**
	 * Adds/sets a player to a character in the game. This method is synchronized to avoid two
	 * players being added at same time and one overwriting the other.
	 * @param newPlayer
	 */
	public synchronized Game addPlayer(String charName, String name) {
		Player newPlayer;
		
		if (this.hasPlayer()) { // the Game has a Player already (this player not VIP)
			newPlayer = new Player(charName, name);
		} else { // this is VIP player
			newPlayer = new Player(charName, name, true);
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

		// TODO: (ALEX) complete logic

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
	 * Returns the player provided their charName
	 * @param charName
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
	 * Returns true if there are currently no player's in the game
	 * @return playerExists
	 */
	public boolean hasPlayer() {

		boolean playerExists = false;
		
		for ( Character character : characterMap.values() ) {
			if (character instanceof Player) {
				playerExists = true;
			}
		}

		return playerExists;

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
}