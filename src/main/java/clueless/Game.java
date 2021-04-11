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
	public List<Card> suggestionCards;
	public Map<String, Character> characterMap = new HashMap<String, Character>();

	public String eventMessage; 

	public Game(int id, Map<String, Character> initMap) { // custom constructor
		this.gameId = id;
		this.mysteryCards = new ArrayList<Card>();
		this.suggestionCards = new ArrayList<Card>();
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
		startingPlayer.state = PLAYER_STATE_MOVE;
		startingPlayer.isTurn = true;
		
		// update possible moves
		this.updatePossibleMoves();

		this.hasStarted = true;
	}

	/**
	 * Changes the player whose turn it is in the the game (should only be executed in when player complete's their turn)
	 */
	public void changeTurn() {

		Player currPlayer = this.getCurrentPlayer();
		Player nextPlayer = this.nextPlayer();
		
		// change currPlayer state to wait
		// change currPlayer isTurn to false
		if (currPlayer.isActive()) { // handle only if player is active (if in 'lose' state just leave them their)
			currPlayer.state = PLAYER_STATE_WAIT;
		}
		currPlayer.isTurn = false;

		// change nextPlayer state to 'move'
		// change nextPlayer isTurn to true
		nextPlayer.state = PLAYER_STATE_MOVE;
		nextPlayer.isTurn = true;

		// update all players possible moves
		this.updatePossibleMoves();

		// clear the suggestion cards for this game
		this.suggestionCards.clear();

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
	 * 
	 */
	public boolean isLocationOccupied(Location location) {
		
		// loop through each character in characterMap and see if their currLocation is location
		for (Character character : this.characterMap.values()) {
			if (character.currLocation.equals(location)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Loops through each Player in characterMap and update's the possible moves based on the follow criteria:
	 * 		- if player NOT in 'move' state then the possible moves should be empty
	 * 		- possible moves should only consist of adjacentLocations (and secret passage added later if exists)
	 * 		- any adjacent location of type: home should not be possible move
	 * 		- any adjacent location of type: hallway that is already occupied should not be possible move
	 * 		- add secretLocation to possibleMoves list if currLocation has one
	 * 
	 */
	public void updatePossibleMoves() {

		Player player;
		List<Location> listOfMoves = new ArrayList<Location>();
		Location currentLocation;
		Location possibleLocation;

		// loop over each player in characterMap
		for (String charName : this.characterMap.keySet()) {
			if (this.isPlayer(charName)) {

				player = this.getPlayer(charName);

				// check if 'move'
				if (player.state == PLAYER_STATE_MOVE) {

					currentLocation = player.currLocation;

					// get list of adjacentLocations
					listOfMoves.clear();
					listOfMoves = getAdjacentLocations(currentLocation);
					
					// remove any locations of type: home
					// remove any locations of type: hallway that are currently occupied
					for (int i = 0; i < listOfMoves.size(); i++) {
						possibleLocation =  listOfMoves.get(i);

						if (possibleLocation.isHome()) {
							listOfMoves.remove(i);
						}

						if (possibleLocation.isHallway() && this.isLocationOccupied(possibleLocation)) {
							listOfMoves.remove(i);
						}
					}
					
					// add secret passage if has one
					if (currentLocation.hasSecretPassage()) {
						listOfMoves.add(currentLocation);
					}
					
					// clear possible moves
					player.possibleMoves.clear();
					
					//dump list into possible moves if not empty
					if (!listOfMoves.isEmpty()) { 
						player.possibleMoves.addAll(listOfMoves); 
					}

				} else {
					player.possibleMoves.clear();
				}
			}
		}


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
		
		// return false if any card is not in the mysterCards
		for (Card card : accusation) {
			if (!this.mysteryCards.contains(card)) {
				return false;
			}
		}

		return true; // return true if all cards are winners
	}

	/**
	 * Returns the player provided their characterId
	 * @param characterId
	 * @return
	 */
	public Character getCharacter(String charName) {

		if (this.characterMap.containsKey(charName)) {
			return this.characterMap.get(charName);
		} else {
			throw new CharacterNotFoundException(charName); // if not found, throw exception
		}

	}

	/**
	 * Returns the player provided their charName
	 * @param charName
	 * @return
	 */
	public Player getPlayer(String charName) {

		if (this.isPlayer(charName)) {
			return (Player) this.getCharacter(charName);
		} else {
			LOGGER.error("getPlayer( " + charName +  " ) could not cast Character to Player");
			return null;
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
	 * Updates the game given the provided winning player
	 * @param charName
	 */
	public void winGame(String charName) {
		// TODO: complete with logic
		Player  = this.getPlayer(charName);

		// set player to ‘win’ state
		winner.state = PLAYER_STATE_WIN;
		this.endGame();
		
	}
	
	/**
	 * Updates the game given the provided losing player
	 * @param charName
	 */
	public void loseGame(String charName) {
		Player loser = this.getPlayer(charName);
		
		// deactivate player
		loser.deactivate();
		
		// set player to 'lose' state
		loser.state = PLAYER_STATE_LOSE;

		// changeTurn()
		this.changeTurn();
		
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
		gameJson.put("suggestionCards", cardsToJsonArray(this.suggestionCards)); 
		gameJson.put("mysteryCards", cardsToJsonArray(this.mysteryCards)); // TODO: obviously remove this later
		gameJson.put("eventMessage", this.eventMessage); 
		gameJson.put("characterMap", charMapToJsonObject(this.characterMap));

		return gameJson;
	}
}
