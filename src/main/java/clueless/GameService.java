package clueless;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/games")
@CrossOrigin(origins = "*", allowedHeaders = "*")
class GameService extends GameDataManager {

	private static final Logger LOGGER = LogManager.getLogger(GameService.class);

	private final int JSON_SPACING = 4;

	/**
	 * Accept a clue for another suggestion
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/suggestion/accept", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> acceptRevealedClueHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);

		// validate 
		if (!playerName.equals(player.playerName)) {
			LOGGER.error(playerName + " was denied requested action because " + charName + " is assigned to player: " + player.playerName);
			return new ResponseEntity<String>(printJsonError("player name does not match provided character's"), HttpStatus.BAD_REQUEST);
		} else {
			// validate player is in accept_reveal state
			if (!player.state.equals(PLAYER_STATE_ACCEPT_REVEAL)) {
				LOGGER.error(player.playerName + " may not accept reveal because they have state: " 
						+ player.state + " (not " + PLAYER_STATE_ACCEPT_REVEAL + ").");
				return new ResponseEntity<String>(printJsonError("player not in correct state to make desired action"), HttpStatus.BAD_REQUEST);
			} else {
				
				// change player's state to complete_turn
				// update player's knownCards
				// clear player's revealedClueCard
				// update gameEventMessage
				player.state = PLAYER_STATE_COMPLETE_TURN;
				player.addKnownCard(player.getRevealedClueCard());
				player.clearRevealedClueCard();
				player.eventMessage = "You have accepted the clue. Please make an accusation or complete your turn.";
				logInfoEvent(game, player.playerName + " has accepted the revealed clue card");
				
			}
		}

		return null;
	}

	//	/**
	//	 * Deletes the provided game
	//	 * @param gid
	//	 */
	//	@DeleteMapping("/{gid}")
	//	void deleteGameHTTP(@PathVariable int gid) {
	//		deleteGame(gid);
	//	}

	/**
	 * Cancels the player's accusation
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/accusation/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> cancelAccusationHTTP(@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName) {

		// TODO: (ALEX) fill with logic

		return null;
	}

	/**
	 * Cancels the player's suggestion
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/suggestion/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> cancelSuggestionHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName) {

		// TODO: (ALEX) fill with logic

		return null;
	}

	/**
	 * Adds a player to the game provided a string of the playerName
	 * @param gid
	 * @param charName
	 * @param name
	 * @return
	 */
	@PostMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createPlayerInGameHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName, 
			@RequestParam(required = true) String playerName) {

		Game game = getGame(gid);
		Character character = game.getCharacter(charName);

		// check if character is already a Player object, return 409 status if so
		if (character instanceof Player) {

			LOGGER.error("The character named " + charName + " is already mapped to a player named "
					+ game.getPlayer(charName).playerName + ".");

			return new ResponseEntity<String>(printJsonError("character was already chosen"), HttpStatus.CONFLICT);

		} else { // update character map with new player

			game.addPlayer(charName, playerName); // add player to game
			logInfoEvent(game, "Player named " + playerName + " added to game " + gid + " as "+ charName + ".");
			

			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
		}
	}

	/**
	 * Creates a new game object (starts new game)
	 * @param name 
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createGameHTTP(
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName) {

		JSONObject newGameJson = addNewGame(charName, playerName).toJson();

		return new ResponseEntity<String>(jsonToString(newGameJson), HttpStatus.OK);
	}

	/**
	 * Completes the player's turn
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/complete-turn", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> completeTurnHTTP(@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		Player nextPlayer = game.getNextPlayer();

		// validate playerName is charName
		// validate charName has state of complete-turn
		if (!playerName.equals(player.playerName)) {
			LOGGER.error(playerName + " was denied complete-turn action because " + charName + " is assigned to player: " + player.playerName);
			return new ResponseEntity<String>(printJsonError("player name does not match provided character's"), HttpStatus.BAD_REQUEST);
		} else {
			if (player.state.equals(PLAYER_STATE_SUGGEST)) {
				player.eventMessage = "You must make a suggestion before completing your turn.";
				LOGGER.error(player.playerName + " must make a suggestion before completing their turn.");
				return new ResponseEntity<String>(printJsonError(player.playerName + " must make a suggestion before completing their turn."), HttpStatus.BAD_REQUEST);
			} else if (!player.state.equals(PLAYER_STATE_COMPLETE_TURN)) {
				LOGGER.error(playerName + " was denied complete-turn action because they are not in complete_turn state.");
				return new ResponseEntity<String>(printJsonError("player may not complete turn as they're not in complete_turn state."), HttpStatus.BAD_REQUEST);
			} else {

				// update game event message
				logInfoEvent(game, player.playerName + " completed their turn. " + game.getNextPlayer().playerName + " is next to make move.");
				player.eventMessage = "You have completed your turn.";
				nextPlayer.eventMessage = "It is your turn, please make your move";
				// change turns
				game.changeTurn();
				

				return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
			}
		}
	}

	/**
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> getAllGamesHTTP() {
		LOGGER.debug("All games returned.");

		return new ResponseEntity<String>(jsonToString(getAllGamesJSON()), HttpStatus.OK);
	}

	/**
	 * Returns the specific game object requested
	 * @param gid
	 * @return gameAsJson
	 */
	@GetMapping(value = "/{gid}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> getGameHTTP(@PathVariable int gid) {
		LOGGER.debug("Game " + gid + " returned.");
		return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.OK);
	}



	//	/**
	//	 * Deletes the provided game
	//	 * @param gid
	//	 */
	//	@DeleteMapping("/{gid}")
	//	void deleteGameHTTP(@PathVariable int gid) {
	//		deleteGame(gid);
	//	}

	/**
	 * Processes the player's accusation provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/accusation/accuse", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeAccusationHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {
		
		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		
		// validate request
		if (!playerName.equals(player.playerName)) {
			LOGGER.error(playerName + " was denied requested action because " + charName + " is assigned to player: " + player.playerName);
			return new ResponseEntity<String>(printJsonError("player name does not match provided character's"), HttpStatus.BAD_REQUEST);
		} else {
			
			// validate it is this player's turn
			if (!player.isTurn) {
				LOGGER.error(playerName + " was denied accusation because is is not their turn.");
				player.eventMessage = "It is not your turn, you cannot make an accusation.";
				return new ResponseEntity<String>(printJsonError("accusation denied because it is not this player's turn"), HttpStatus.BAD_REQUEST);
			} else {
				
				Card weaponCard = CARD_MAP.get(weapon);
				Card roomCard = CARD_MAP.get(room);
				Card suspectCard = CARD_MAP.get(suspect);
				
				// validate all card types are provided accurately
				if (weaponCard.isType(CARD_TYPE_WEAPON) && roomCard.isType(CARD_TYPE_ROOM) && suspectCard.isType(CARD_TYPE_SUSPECT)) {
					List<Card> accusationCards = new ArrayList<Card>();
					accusationCards.add(weaponCard);
					accusationCards.add(roomCard);
					accusationCards.add(suspectCard);
					
					// win or lose game
					if (game.isAccusationCorrect(accusationCards)) {
						game.winGame(player.characterName);
						logInfoEvent(game, player.playerName + " won the game with his/her provided accusation!");
						player.eventMessage = "Your accusation is correct, you win!";
						
						// TODO: handle game cleanup in database
						
					} else {
						game.loseGame(player.characterName);
						logInfoEvent(game, player.playerName + " lost the game with his/her provided accusation!");
						player.eventMessage = "Your accusation is incorrect, you lose.";
					}
					
					return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
					
				} else {
					LOGGER.error("One of the cards in accusation is not the correct type.");
					player.eventMessage = "One of your cards in the accusation in the incorrect category, please retry.";
					return new ResponseEntity<String>(printJsonError("One of the cards in accusation is not the correct type."), HttpStatus.BAD_REQUEST);
				}
			}
		}
	}

	/**
	 * Processes the player's suggestion provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/suggestion/suggest", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeSuggestionHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {

		Game game = getGame(gid);
		Player suggester = game.getPlayer(charName);
		Character suspectChar = game.getCharacter(suspect);
		Location suggestedLocation = LOCATION_MAP.get(room);

		// check that player is either in ('suggest' state) OR ('move' state AND wasMovedToRoom)
		if ((!suggester.state.equals(PLAYER_STATE_SUGGEST)) 
				&& (!(suggester.state.equals(PLAYER_STATE_MOVE) && suggester.wasMovedToRoom))) {
			LOGGER.error(suggester.playerName + " not in valid state to make suggestion (state: " + suggester.state+ ")");
			//suggester.eventMessage = "You cannot make a suggestion yet.";
			return new ResponseEntity<String>(printJsonError("player not in valid state to make suggestion"), HttpStatus.BAD_REQUEST);
		} else {

			// validate room is player's currLocation
			if (suggester.currLocation.equals(suggestedLocation)) {

				// set suggester player state to await_reveal
				suggester.state = PLAYER_STATE_AWAIT_REVEAL;

				// set suggester player wasMovedToRoom to false (in case it was prevously true)
				suggester.setMovedToRoom(false);

				// set suspect's current location to the provided room
				suspectChar.currLocation = suggestedLocation;

				// set suspect's wasMovedToRoom to true
				suspectChar.setMovedToRoom(true);

				// determine who has clue
				List<Card> suggestion = new ArrayList<Card>();
				suggestion.add(CARD_MAP.get(weapon));
				suggestion.add(CARD_MAP.get(room));
				suggestion.add(CARD_MAP.get(suspect));

				game.suggestionCards.clear();
				game.suggestionCards.addAll(suggestion); // update game suggestion cards

				Player playerWithClue = game.whoHasClue(suggestion);

				if (Objects.isNull(playerWithClue)) { // if null, no one has clue -- set suggester to complete_turn

					logInfoEvent(game, suggester.playerName + " made a suggestion that no one has a clue for.");
					suggester.eventMessage = "No one had a clue for your suggestion. Please complete your turn or make an accusation.";
					suggester.state = PLAYER_STATE_COMPLETE_TURN;

				} else { // else, playerWithClue should be set to reveal

					logInfoEvent(game, suggester.playerName + " made a suggestion that " + playerWithClue.playerName + " must reveal a clue for.");
					suggester.eventMessage = "Waiting for clue from " + playerWithClue.playerName;
					playerWithClue.eventMessage = "Please reveal a clue for the provided suggestion.";
					playerWithClue.state = PLAYER_STATE_REVEAL;
				}
				
				return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);

			} else { // return 400 (BAD_REQUEST)
				LOGGER.error(suggester.playerName + " not in room provided in suggestion (room: " + suggester.currLocation + ")");
				suggester.eventMessage = "Sorry, your suggestion must include the room you're in.";
				return new ResponseEntity<String>(printJsonError("player not in room provided in suggestion"), HttpStatus.BAD_REQUEST);
			}
		}
	}

	/**
	 * Reveal a clue for another suggestion
	 * @param gid
	 * @param charName
	 * @param cardName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/suggestion/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> revealClueHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName,
			@RequestParam(required = true) String cardName) {

		Game game = getGame(gid);
		Player revealer = game.getPlayer(charName);
		Player suggester = game.getCurrentPlayer();
		Card revealedCard = CARD_MAP.get(cardName);

		// if revealer is in 'reveal' state
		// if revealer has cardName
		// if cardName is in game's current suggestion
		if (revealer.state == PLAYER_STATE_REVEAL) {
			if (revealer.hasClue(game.suggestionCards)) {
				if (game.suggestionCards.contains(revealedCard)) {

					// update revealer's state to 'wait'
					// update suggester's revealedClueCard
					// update suggester to have state 'accept_reveal'
					revealer.state = PLAYER_STATE_WAIT;
					suggester.setRevealedClueCard(revealedCard);
					suggester.state = PLAYER_STATE_ACCEPT_REVEAL;

					// update player and game eventMessages
					logInfoEvent(game, revealer.playerName + " revealed a clue to " + suggester.playerName + ".");
					revealer.eventMessage = "You revealed a clue, please hold tight and await your turn.";
					suggester.eventMessage = revealer.playerName + " revealed a clue to you, please accept the clue.";

					return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);

				} else {
					return new ResponseEntity<String>(printJsonError("player does not have clue in suggestion"), HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<String>(printJsonError("player does not have the clue card provided"), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<String>(printJsonError("player not in reveal state"), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Starts the specific game object by changing the 
	 * game's attribute hasStarted to true
	 * @param gid
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> startGameHTTP(
			@PathVariable int gid, 
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName,
			@RequestParam(required = true) Boolean activate) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		Player startingPlayer = game.startingPlayer();

		// return 400 (BAD_REQUEST) if not VIP
		if (!(player.vip)) { 
			return new ResponseEntity<String>(printJsonError("player not vip"), HttpStatus.BAD_REQUEST);
		} else { 
			// check if startName is true
			if (activate) {
				game.startGame(); // initiate game's start game sequence
				logInfoEvent(game, "Game " + gid + " was started by " + player.playerName + ". Starting player is " + startingPlayer.playerName);
				player.eventMessage = "You have started a new game.";
			} else {
				LOGGER.info(player.playerName + " send startGame but equal to true.");
			}
			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
		}
	}

	/**
	 * Updates the character's location
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/location", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> moveCharacterHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String playerName,
			@RequestParam(required = true) String locName) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		Location location = LOCATION_MAP.get(locName);

		if (!playerName.equals(player.playerName)) {
			LOGGER.error(playerName + " was denied move to " + locName + " because " + charName + " is assigned to player: " + player.playerName);
			return new ResponseEntity<String>(printJsonError("player name does not match provided character's"), HttpStatus.BAD_REQUEST);
		} else {
			// validate character is in 'move' state
			if (!player.state.equals(PLAYER_STATE_MOVE)) {
				LOGGER.error(player.playerName + " was denied move to " + locName + " because their state is: " + player.state + " (not "
						+ PLAYER_STATE_MOVE + ").");
				player.eventMessage = "You are not able to move yet.";
				return new ResponseEntity<String>(printJsonError("player not in move state"), HttpStatus.BAD_REQUEST);
			} else {
				// if hallway and location occupied return 409 (CONFLICT)
				if (location.isHallway() && game.isLocationOccupied(location)) {
					LOGGER.error(player.playerName + " was denied move to " + locName + " because it's occupied.");
					player.eventMessage = "This hallway is occupied, cannot move here.";
					return new ResponseEntity<String>(printJsonError("hallway is occupied"), HttpStatus.CONFLICT);
				} else {

					// update character's current location
					player.currLocation = location;

					if (location.isRoom()) { // if room, prompt to make suggestion
						player.state = PLAYER_STATE_SUGGEST;

						// update game eventMessage
						game.eventMessage = player.playerName + " moved " + charName + " to the " + locName;
						player.eventMessage = "You moved to room " + locName + ", please make a suggestion";

					} else { // else, prompt to complete turn
						player.state = PLAYER_STATE_COMPLETE_TURN;

						// update game eventMessage (for hallways the naming convention probably doesn't matter to users)
						game.eventMessage = player.playerName + " moved " + charName + " to a " + location.type;
						player.eventMessage = "You have moved to a hallway. Please make an accusation or complete your turn.";
					}
					
					// update possible moves (really to clear possible moves)
					game.updatePossibleMoves();

					LOGGER.info(player.playerName + " moved " + charName + " to the " + locName);
					return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
				}
			}
		}

	}

	/**
	 * Returns string representing json form for error messages to frontend
	 * @return
	 */
	String printJsonError(String errorMessage) {
		JSONObject errorJson = new JSONObject();

		errorJson.put("error", errorMessage);

		return jsonToString(errorJson);

	}

	/**
	 * Returns string of a given JSON
	 * @return
	 */
	String jsonToString(Object json) {
		if (json != null) {
			if ( json instanceof JSONArray ) {
				JSONArray jsonArray = (JSONArray) json;
				return jsonArray.toString(JSON_SPACING) + '\n';
			} else if (json instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) json;
				return jsonObject.toString(JSON_SPACING) + '\n';
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Updates game's eventMessage and log's the statement
	 * @return
	 */
	void logInfoEvent(Game game, String message) {
		game.eventMessage = message;
		LOGGER.info(message);
	}
}
