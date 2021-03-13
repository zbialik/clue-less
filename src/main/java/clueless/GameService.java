package clueless;

import java.util.HashMap;

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
class GameService {
	
    private static final Logger LOGGER = LogManager.getLogger(GameService.class);
    
    private final int JSON_SPACING = 4;
	private int gameId = 1;
	private final HashMap<Integer, Game> gamesHashMap;

	GameService() {
		this.gamesHashMap = new HashMap<Integer, Game>();
	}

	/**
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getAllGames() {
		LOGGER.info("All games returned.");
//		return toStringAllGames();
		return new ResponseEntity<String>(toStringAllGames(), HttpStatus.OK);
	}
	 
	/**
	 * Creates a new game object (starts new game)
	 * @param name (opt)
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> createGame(@RequestParam(required = false) String name) {
		
		Game newGame = new Game(this.gameId);
		
		if (!(name.isBlank() || name.isEmpty() || name == "")) {
			newGame.addPlayer(new Player(name)); // include player in initialized game
			LOGGER.info(name + " initialized Game " + this.gameId + ".");
		} else {
			LOGGER.info("Initialized empty Game " + this.gameId + ".");
		}
		
		this.gamesHashMap.put(this.gameId, newGame); // initialize new game
		this.gameId++; // increment game ID to set unique ID's for games
		
		return new ResponseEntity<String>(this.toStringGameByID(this.gameId - 1), HttpStatus.OK);
	}

	/**
	 * Returns the specific game object requested
	 * @param gid
	 * @return
	 */
	@GetMapping(value = "/{gid}", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getGame(@PathVariable int gid) {
		LOGGER.info("Game " + gameId + " returned.");
		return new ResponseEntity<String>(this.toStringGameByID(gid), HttpStatus.OK);
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	@CrossOrigin(origins = "http://localhost:4200")
	void deleteGame(@PathVariable int gid) {
		LOGGER.info("Game " + gameId + " deleted.");
		this.gamesHashMap.remove(gid);
	}

	/**
	 * Returns list of players for a given game
	 * @return
	 */
	@GetMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> getAllPlayersInGame(@PathVariable int gid) {
		LOGGER.info("Players in game " + gameId + " returned.");
		return new ResponseEntity<String>(this.toStringAllPlayersInGame(gid), HttpStatus.OK);
	}

	/**
	 * Adds a player to the game provided a string of the playerName
	 * @param name
	 * @param gid
	 * @return
	 */
	@PostMapping(value = "/{gid}/players", produces = MediaType.APPLICATION_JSON_VALUE)
	@CrossOrigin(origins = "http://localhost:4200")
	ResponseEntity<String> createPlayerInGame(@RequestParam(required = true) String name, @PathVariable int gid) {
		this.gamesHashMap.get(gid).addPlayer(new Player(name)); // add player to game
		
		LOGGER.info("Player named " + name + " added to game " + gameId + ".");
		return new ResponseEntity<String>(this.toStringAllPlayersInGame(gid), HttpStatus.OK);
	}

	/**
	 * Returns string of all games
	 * @return
	 */
	String toStringAllGames() {
		return toJsonAllGames().toString(JSON_SPACING) + '\n'; // apply pretty formatting;
	}
	
	/**
	 * Returns json array of all games
	 * @return
	 */
	JSONArray toJsonAllGames() {
		
		JSONArray allGamesJson = new JSONArray();
		for (Game game : this.gamesHashMap.values()) {
			allGamesJson.put(game.toJson());
		}

		return allGamesJson;
	}
	
	/**
	 * Returns string of game data
	 * @param gid
	 * @return
	 */
	String toStringGameByID(int gid) {
		return this.gamesHashMap.get(gid).toString(JSON_SPACING) + '\n'; // apply pretty formatting;
	}

	/**
	 * Returns string of all players in a game
	 * @param gid
	 * @return
	 */
	String toStringAllPlayersInGame(int gid) {
		return toJsonAllPlayersInGame(gid).toString(JSON_SPACING) + '\n'; // apply pretty formatting;
	}

	/**
	 * Returns JSONArray representing all players in a game
	 * @param gid
	 * @return
	 */
	JSONArray toJsonAllPlayersInGame(int gid) {

		JSONArray playersJson = new JSONArray();
		for (Player player : this.gamesHashMap.get(gid).getPlayers().values()) {
			playersJson.put(player.toJson());
		}

		return playersJson;
	}
}