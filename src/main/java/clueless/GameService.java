package clueless;

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

		// TODO: (low-priority) verify player is VIP to allow startGame action, return 409/CONFLICT if not VIP
		
		if (startGame) {
			getGame(gid).startGame(); // add player to game
			LOGGER.info("Game " + gid + " was started by " + getGame(gid).getPlayer(charName).playerName);
		} else {
			LOGGER.info("Game " + gid + " was stopped by " + getGame(gid).getPlayer(charName).playerName);
		}
		
		return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.OK);
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	void deleteGameHTTP(@PathVariable int gid) {
		LOGGER.info("Game " + gid + " deleted.");
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

		// check if character is already a Player object, return 409 status if so
		if (getGame(gid).getCharacter(charName) instanceof Player) {
			LOGGER.info("The character named " + charName + " is already mapped to a player named "
					+ getGame(gid).getPlayer(charName).playerName + ".");
			return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.CONFLICT);
		} else {
			// update character map with new player
			getGame(gid).addPlayer(charName, name); // add player to game
			LOGGER.info("Player named " + name + " added to game " + gid + ".");
			return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.OK);
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
			@RequestParam(required = true) String charName) {

		// TODO: (ZACH) fill with logic

		return null;
	}

	/**
	 * Processes the player's accusation provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/accusation", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeAccusationHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {

		// TODO: (ALEX) fill with logic

		return null;
	}

	/**
	 * Processes the player's accusation provided
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/suggestion", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> makeSuggestionHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName,
			@RequestParam(required = true) String weapon,
			@RequestParam(required = true) String room,
			@RequestParam(required = true) String suspect) {

		// TODO: (ZACH) fill with logic

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

		// TODO: (ZACH) fill with logic

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
			@RequestParam(required = true) String cardName) {
		
		// TODO: (ZACH) fill with logic

		return null;
	}
	
	/**
	 * Cancels the player's accusation
	 * @param gid
	 * @param charName
	 * @return gameAsJson
	 */
	@PostMapping(value = "/{gid}/players/reveal", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> revealClueHTTP(
			@PathVariable int gid,
			@RequestParam(required = true) String charName) {

		// TODO: (ZACH) fill with logic

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