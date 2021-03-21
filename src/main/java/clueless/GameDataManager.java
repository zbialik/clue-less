package clueless;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

public interface GameDataManager {

	static final Logger LOGGER = LogManager.getLogger(GameService.class);
	
	static final String GAME_DATASTORE_JSON_FILE = System.getProperty("user.dir") + "/gameDataStore.json";

	// game datastore
	HashMap<Integer, Game> gamesHashMap = new HashMap<Integer, Game>();
	
	// TODO: insert section for reading JSON file if it exists and reloading game datastore

	/**
	 * Returns json array of all games
	 * @return allGamesJson
	 */
	default JSONArray getAllGames() {
		JSONArray allGamesJson = new JSONArray();
		for (Game game : gamesHashMap.values()) {
			allGamesJson.put(game.toJson());
		}

		return allGamesJson;
	}

	/**
	 * Returns JSONObject representing the given game
	 * @param gid
	 * @return gameJson
	 */
	default JSONObject getGame(int gid) {
		return gamesHashMap.get(gid).toJson();
	}

	/**
	 * Returns JSONArray representing all players in a game
	 * @param gid
	 * @return playersInGameJson
	 */
	default JSONArray getAllPlayersInGame(int gid) {

		JSONArray playersJson = new JSONArray();
		for (Player player : gamesHashMap.get(gid).getPlayers().values()) {
			playersJson.put(player.toJson());
		}

		return playersJson;
	}

	/**
	 * Adds new game to datastore and returns the game created
	 * @param name
	 * @param newGameId
	 * @return newGameJson
	 */
	default JSONObject addNewGame(String name, int newGameId) {

		Game newGame = new Game(newGameId);

		if (!(name.isBlank() || name.isEmpty() || name == "")) {
			newGame.addPlayer(new Player(name)); // include player in initialized game
			LOGGER.info(name + " initialized Game " + newGameId + ".");
		} else {
			LOGGER.info("Initialized empty Game " + newGameId + ".");
		}

		gamesHashMap.put(newGameId, newGame); // initialize new game

		return newGame.toJson();
	}
	
	/**
	 * Adds new player to the given game and returns game as JSON
	 * @param name
	 * @param gameId
	 * @return gameJson
	 */
	default JSONObject addNewPlayerInGame(String name, int gameId) {

		gamesHashMap.get(gameId).addPlayer(new Player(name)); // add player to game
		
		return gamesHashMap.get(gameId).toJson();
	}

	default void deleteGame(int gameId) {
		gamesHashMap.remove(gameId);
	}

}
