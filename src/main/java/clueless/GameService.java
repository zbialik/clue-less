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
	ResponseEntity<String> createGameHTTP(@RequestParam(required = true) String name, @RequestParam(required = true) String charName) {
		
		JSONObject newGameJson = addNewGame(name, charName).toJson();

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
			@RequestParam(required = true) String startGame, 
			@RequestParam(required = true) String name, 
			@PathVariable int gid) {
		
		gamesHashMap.get(gid).startGame(); // add player to game

		LOGGER.info("Game " + gid + " was started by " + name);
		
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
	 * @param name
	 * @param gid
	 * @return
	 */
	@PostMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createPlayerInGameHTTP(@RequestParam(required = true) String name, 
			@RequestParam(required = true) String charName, 
			@PathVariable int gid) {
		
		
		gamesHashMap.get(gid).setPlayerToCharacter(new Player(charName, name)); // add player to game

		LOGGER.info("Player named " + name + " added to game " + gid + ".");
		
		return new ResponseEntity<String>(jsonToString(getGame(gid).toJson()), HttpStatus.OK);
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