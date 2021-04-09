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

	public Game(int id, Map<String, Character> initMap) { // custom constructor
		this.gameId = id;
		this.mysteryCards = new ArrayList<Card>();
		this.hasStarted = false;
		this.eventMessage = new String();

		// copy character map from interface to this game
		this.characterMap.putAll(initMap);
	}

	/**
	 * Starts the game by dealing cards to players
	 * and changing hasStarted to true
	 */
	public void startGame() {

		// deal player's cards
		dealCards(this);

		// set the starting player's turn
		Player startingPlayer = this.startingPlayer();
		startingPlayer.state = PLAYER_STATE_WAIT;
		startingPlayer.isTurn = true;

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
	 * Returns the player whose turn it currently is
	 * NOTE: There should only be one player with isTurn set to true 
	 * 		 at any moment during the game
	 * 
	 * @return currentPlayer
	 */
	public Player getCurrentPlayer() {

		Player currPlayer;

		for (Character character : this.characterMap.values()) {
			if (this.isPlayer(character)) {
				currPlayer = (Player) character;
				if (currPlayer.isTurn) {
					return currPlayer;
				}
			}
		}

		return null;
	}

	/**
	 * Returns the next player whose turn it is in the game
	 * @return nextPlayer
	 */
	public Player nextPlayer() {

		String currCharacterName = this.getCurrentPlayer().characterName;
		
		// find index of currCharacterName in CHARACTER_TURN_ORDER
		int currIndex = getCharacterIndexInTurnOrder(currCharacterName);
		
		// get index of next character in CHARACTER_TURN_ORDER (to start loop)
		int index = currIndex + 1;
		if (index >= CHARACTER_TURN_ORDER.length) { // repoint index if currIndex is last character
			index = 0;
		}
		
		// loop through CHARACTER_TURN_ORDER and find next player that isActive()
		int count = 0;
		while (count < CHARACTER_TURN_ORDER.length) {
			
			if (this.isPlayer(CHARACTER_TURN_ORDER[index]) 
					&& this.getPlayer(CHARACTER_TURN_ORDER[index]).isActive()) {
				return this.getPlayer(CHARACTER_TURN_ORDER[index]);
			} else {
				index++;
				count++;
				
				if (index >= CHARACTER_TURN_ORDER.length) { // restart index if reached last character
					index = 0;
				}
			}
		}

		return null;
	}
	
	/**
	 * Returns true if the provided name of a Character is a player
	 * @param charName (String)
	 * @return isPlayer
	 */
	public boolean isPlayer(String charName) {
		
		if (this.getCharacter(charName) instanceof Player) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns true of the provided Character is a player
	 * @param character (Character)
	 * @return isPlayer
	 */
	public boolean isPlayer(Character character) {
		
		if (character instanceof Player) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the player whose turn it is when starting the game
	 * @return startingPlayer
	 */
	public Player startingPlayer() {
		
		for (int i = 0; i < CHARACTER_TURN_ORDER.length; i++) {
			if (this.isPlayer(CHARACTER_TURN_ORDER[i])) {
				return ((Player) this.characterMap.get(CHARACTER_TURN_ORDER[i]));
			}
		}
		
		return null; // return null if couldn't find a player
	}

	/**
	 * Helper method for determining if a location is occupied in this game 
	 * returns true if location is occupied, false otherwise
	 * @param location
	 * @return occupied
	 */
	public boolean isLocationOccupied(Location location) {
		boolean occupied = false;

		// TODO: (ALEX) complete logic below

		// loop through each character in characterMap and see if their currLocation is location
		// 	(use the location.equals() method to determine)

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
	public synchronized Game addPlayer(String charName, String name, Location homeLocation) {
		Player newPlayer;

		if (this.hasPlayer()) { // the Game has a Player already (this player not VIP)
			newPlayer = new Player(charName, name, homeLocation);
		} else { // this is VIP player
			newPlayer = new Player(charName, name, true, homeLocation);
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
			if (this.isPlayer(charName)) {
				return (Player) this.getCharacter(charName);
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
			if (this.isPlayer(character)) {
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
		Player pl;
		boolean playerHasClue = false;

		int i = 0;
//		while ((!playerHasClue) && i < this.characterMap.size()) {
//			
//		}

		return null;
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