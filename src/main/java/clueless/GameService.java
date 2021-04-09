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
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> getAllGamesHTTP() {
		LOGGER.info("All games returned.");
		
		return new ResponseEntity<String>(jsonToString(getAllGamesJSON()), HttpStatus.OK);
	}

	/**
	 * Creates a new game object (starts new game)
	 * @param name 
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createGameHTTP(
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String name) {

		JSONObject newGameJson = addNewGame(charName, name).toJson();

		return new ResponseEntity<String>(jsonToString(newGameJson), HttpStatus.OK);
	}

	/**
	 * Returns the specific game object requested
	 * @param gid
	 * @return gameAsJson
	 */
	@GetMapping(value = "/{gid}", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> getGameHTTP(@PathVariable int gid) {
		LOGGER.info("Game " + gid + " returned.");
		return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.OK);
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
			@RequestParam(required = true) Boolean startGame) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		
		// return 400 (BAD_REQUEST) if not VIP
		if (!(player.vip)) { 
			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.BAD_REQUEST);
		} else { 
			// check if startName is true
			if (startGame) {
				game.startGame(); // initiate game's start game sequence
				LOGGER.info("Game " + gid + " was started by " + player.playerName);
				game.eventMessage = "Game " + gid + " was started by " + player.playerName;
			} else {
				LOGGER.info(player.playerName + " send startGame but equal to true.");
			}
			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
		}
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	void deleteGameHTTP(@PathVariable int gid) {
		deleteGame(gid);
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
			@RequestParam(required = true) String name) {

		Game game = getGame(gid);
		Character character = game.getCharacter(charName);
		
		// check if character is already a Player object, return 409 status if so
		if (character instanceof Player) {

			LOGGER.error("The character named " + charName + " is already mapped to a player named "
					+ game.getPlayer(charName).playerName + ".");

			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.CONFLICT);

		} else { // update character map with new player

			game.addPlayer(charName, name, getCharacterHome(charName)); // add player to game
			LOGGER.info("Player named " + name + " added to game " + gid + " as "+ charName + ".");
			game.eventMessage = "Player named " + name + " added to the game as " + charName + ".";

			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
		}
	}

	/**
	 * Updates the character's location
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/location", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> updatePlayerLocationHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String locName) {

		Game game = getGame(gid);
		Player player = game.getPlayer(charName);
		Location location = LOCATION_MAP.get(locName);
		
		// validate character is in 'move' state
		if (player.state != PLAYER_STATE_MOVE) {
			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.BAD_REQUEST);
		} else {
			// if hallway and location occupied return 409 (CONFLICT)
			if (location.isHallway() && game.isLocationOccupied(location)) {
				return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.CONFLICT);
			} else {

				// update character's current location
				player.setCurrLocation(location);

				if (location.isRoom()) { // if room, prompt to make suggestion
					player.state = PLAYER_STATE_SUGGEST;

					// update game eventMessage
					game.eventMessage = player.playerName + " moved " + charName + " to the " + locName;

				} else { // else, prompt to complete turn
					player.state = PLAYER_STATE_COMPLETE_TURN;

					// update game eventMessage
					game.eventMessage = player.playerName + " moved " + charName + " to a " + location.type;
				}

				return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.OK);
			}
		}
	}

	/**
	 * Processes the player's accusation provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/accusation/accuse", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeAccusationHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {

		// TODO: (ALEX) fill with logic

		// TODO: (low-priority) update game eventMessage

		return null;
	}

	/**
	 * Processes the player's suggestion provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/suggestion/suggest", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeSuggestionHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {
		
		Game game = getGame(gid);
		Player suggester = game.getPlayer(charName);
		Character suspectChar = game.getCharacter(suspect);
		Location suggestedLocation = LOCATION_MAP.get(room);
		
		// check that player is either in ('suggest' state) OR ('move' state AND wasMovedToRoom)
		if (!((suggester.state != PLAYER_STATE_SUGGEST) 
				|| ((suggester.state != PLAYER_STATE_MOVE) && suggester.wasMovedToRoom))) {
			return new ResponseEntity<String>(jsonToString(game.toJson()), HttpStatus.BAD_REQUEST);
		} else {
			
			// validate room is player's currLocation
			if (suggester.getCurrLocation().equals(suggestedLocation)) {
				
				// set suggester player state to await_reveal
				suggester.state = PLAYER_STATE_AWAIT_REVEAL;
				
				// set suggester player wasMovedToRoom to false (in case it was prevously true)
				suggester.setMovedToRoom(false);
				
				// set suspect's current location to the provided room
				suspectChar.setCurrLocation(suggestedLocation);
				
				// set suspect's wasMovedToRoom to true
				suspectChar.setMovedToRoom(true);
				
				// determine who has clue
				List<Card> suggestion = new ArrayList<Card>();
				suggestion.add(CARD_MAP.get(weapon));
				suggestion.add(CARD_MAP.get(room));
				suggestion.add(CARD_MAP.get(suspect));
				
				Player playerWithClue = game.whoHasClue(suggestion);
				
				// TODO: verify this logic is sound
				if (Objects.isNull(playerWithClue)) { // if null, no one has clue -- set suggester to complete_turn
					
					game.eventMessage = suggester.playerName + " made a suggestion that no one has a clue for ";
					suggester.setEventMessage("No one had a clue for your suggestion. Please complete your turn or make an accusation.");
					suggester.state = PLAYER_STATE_COMPLETE_TURN;
					
				} else { // else, playerWithClue should be set to reveal
					
					game.eventMessage = suggester.playerName + " made a suggestion that " + playerWithClue.playerName + " must reveal a clue for.";
					playerWithClue.setEventMessage("Please reveal a clue for the provided suggestion.");
					playerWithClue.state = PLAYER_STATE_REVEAL;
				}
				
			} else { // return 400 (BAD_REQUEST)
				return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.BAD_REQUEST);
			}
		}

		// TODO: (low-priority) update game eventMessage

		return null;
	}

	/**
	 * Cancels the player's suggestion
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/suggestion/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> cancelSuggestionHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName) {

		// TODO: (ALEX) fill with logic

		return null;
	}

	/**
	 * Cancels the player's accusation
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/accusation/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> cancelAccusationHTTP(@PathVariable int gid,
			@RequestParam(required = true) String charName) {

		// TODO: (ALEX) fill with logic

		return null;
	}

	/**
	 * Reveal a clue for another suggestion
	 * @param gid
	 * @param charName
	 * @param cardName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/suggestion/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> revealClueHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String cardName) {

		// TODO: (ZACH) fill with logic

		// TODO: (low-priority) update game eventMessage
		
		

		return null;
	}

	/**
	 * Accept a clue for another suggestion
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/suggestion/accept", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> acceptRevealedClueHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName) {

		// TODO: (MEGAN) fill with logic

		// TODO: (low-priority) update game eventMessage

		return null;
	}

	/**
	 * Completes the player's turn
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/complete-turn", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> completeTurnHTTP(@PathVariable int gid,
			@RequestParam(required = true) String charName) {

		// TODO: (ALEX) fill with logic

		// TODO: (low-priority) update game eventMessage
		
		// TODO: make sure to update isTurn for the player and nextPlayer() 

		return null;
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
}