package clueless;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
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
class GameController {
	
    private static final Logger LOGGER = LogManager.getLogger(GameController.class);
    

	private int gameId = 1;
	private final HashMap<Integer, Game> gamesHashMap;

	GameController() {
		this.gamesHashMap = new HashMap<Integer, Game>();
	}

	/**
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	String getAllGames() {
		LOGGER.info("All games returned.");
		return toStringAllGames();
	}

	/**
	 * Creates a new game object (starts new game)
	 * @param newGame
	 * @return
	 */
	@PostMapping()
	String createGame() {
		this.gamesHashMap.put(gameId, new Game(gameId)); // initialize empty new game

		LOGGER.info("Game " + gameId + " created.");
		gameId++; // increment game ID to set unique ID's for games

		return toStringAllGames();
	}

	/**
	 * Returns the specific game object requested
	 * @param gid
	 * @return
	 */
	@GetMapping("/{gid}")
	String getGame(@PathVariable int gid) {
		LOGGER.info("Game " + gameId + " returned.");
		return this.gamesHashMap.get(gid).toString();
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	void deleteGame(@PathVariable int gid) {
		LOGGER.info("Game " + gameId + " deleted.");
		this.gamesHashMap.remove(gid);
	}

	/**
	 * Returns list of players for a given game
	 * @return
	 */
	@GetMapping("/{gid}/players")
	String getAllPlayersInGame(@PathVariable int gid) {
		LOGGER.info("Players in game " + gameId + " returned.");
		return toStringAllPlayersInGame(gid);
	}

	/**
	 * Adds a player to the game provided a string of the playerName
	 * @param name
	 * @param gid
	 * @return
	 */
	@PostMapping("/{gid}/players")
	String createPlayerInGame(@RequestParam("name") String name, @PathVariable int gid) {
		this.gamesHashMap.get(gid).addPlayer(new Player(name)); // add player to game
		
		LOGGER.info("Player named " + name + " added to game " + gameId + ".");
		return this.toStringAllPlayersInGame(gid);
	}

	/**
	 * Returns string of all games
	 * @return
	 */
	String toStringAllGames() {
		return toJsonAllGames().toString(4) + '\n'; // apply pretty formatting;
	}
	
	/**
	 * Returns string of all games
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
	 * Returns string of all players in a game
	 * @param gid
	 * @return
	 */
	String toStringAllPlayersInGame(int gid) {
		return toJsonAllPlayersInGame(gid).toString(4) + '\n'; // apply pretty formatting;
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