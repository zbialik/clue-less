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
class GameService extends GameController {

	private static final Logger LOGGER = LogManager.getLogger(GameService.class);

	private final int JSON_SPACING = 4;
	private int gameId = 1;

	/**
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getAllGamesHTTP() {
		LOGGER.info("All games returned.");
		return new ResponseEntity<String>(jsonToString(getAllGames()), HttpStatus.OK);
	}

	/**
	 * Creates a new game object (starts new game)
	 * @param name (opt)
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> createGameHTTP(@RequestParam(required = false) String name) {
		
		JSONObject newGameJson = addNewGame(name, this.gameId);
		
		this.gameId++; // increment game ID to set unique ID's for games

		return new ResponseEntity<String>(jsonToString(newGameJson), HttpStatus.OK);
	}

	/**
	 * Returns the specific game object requested
	 * @param gid
	 * @return
	 */
	@GetMapping(value = "/{gid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getGameHTTP(@PathVariable int gid) {
		LOGGER.info("Game " + gid + " returned.");
//		return new ResponseEntity<String>(this.toStringGameByID(gid), HttpStatus.OK);
		return new ResponseEntity<String>(jsonToString(getGame(gid)), HttpStatus.OK);
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	@CrossOrigin(origins = "http://localhost:4200")
	void deleteGameHTTP(@PathVariable int gid) {
		LOGGER.info("Game " + gid + " deleted.");
//		GameDataManager.gamesHashMap.remove(gid);
		
		deleteGame(gid);
	}

	/**
	 * Returns list of players for a given game
	 * @return
	 */
	@GetMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getAllPlayersInGameHTTP(@PathVariable int gid) {
		LOGGER.info("Players in game " + gid + " returned.");
//		return new ResponseEntity<String>(this.toStringAllPlayersInGame(gid), HttpStatus.OK);
		return new ResponseEntity<String>(jsonToString(getAllPlayersInGame(gid)), HttpStatus.OK);
	}

	/**
	 * Adds a player to the game provided a string of the playerName
	 * @param name
	 * @param gid
	 * @return
	 */
	@PostMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> createPlayerInGameHTTP(@RequestParam(required = true) String name, @PathVariable int gid) {
//		GameDataManager.gamesHashMap.get(gid).addPlayer(new Player(name)); // add player to game
		addNewPlayerInGame(name, gid);

		LOGGER.info("Player named " + name + " added to game " + gid + ".");
//		return new ResponseEntity<String>(this.toStringAllPlayersInGame(gid), HttpStatus.OK);
		
		return new ResponseEntity<String>(jsonToString(getGame(gid)), HttpStatus.OK);
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