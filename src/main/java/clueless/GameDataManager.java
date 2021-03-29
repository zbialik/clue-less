package clueless;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONArray;

public class GameDataManager {

	static final Logger LOGGER = LogManager.getLogger(GameService.class);
	
	static final String GAME_DATASTORE_JSON_FILE = System.getProperty("user.dir") + "/gameDataStore.json";

	// game datastore
	public HashMap<Integer, Game> gamesHashMap = new HashMap<Integer, Game>();
	private int gameIdCounter = 1;
	
	// TODO: insert section for reading JSON file if it exists and reloading game datastore

	/**
	 * Returns json array of all games
	 * @return allGamesJson
	 */
	public JSONArray getAllGames() {
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
	public JSONObject getGame(int gid) {
		return gamesHashMap.get(gid).toJson();
	}

	/**
	 * Returns JSONArray representing all players in a game
	 * @param gid
	 * @return playersInGameJson
	 */
	public JSONArray getAllPlayersInGame(int gid) {

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
	public JSONObject addNewGame(String name) {

		Game newGame = new Game(this.gameIdCounter);

		if (!(name.isBlank() || name.isEmpty() || name == "")) {
			newGame.addPlayer(new Player(name)); // include player in initialized game
			LOGGER.info(name + " initialized Game " + this.gameIdCounter + ".");
		} else {
			LOGGER.info("Initialized empty Game " + this.gameIdCounter + ".");
		}

		gamesHashMap.put(this.gameIdCounter, newGame); // initialize new game

		this.gameIdCounter++; // increment game ID to set unique ID's for games

		return newGame.toJson();
	}
	
	/**
	 * Adds new player to the given game and returns game as JSON
	 * @param name
	 * @param gameId
	 * @return gameJson
	 */
	public JSONObject addNewPlayerInGame(String name, int gameId) {

		gamesHashMap.get(gameId).addPlayer(new Player(name)); // add player to game
		
		return gamesHashMap.get(gameId).toJson();
	}

	public void deleteGame(int gameId) {
		gamesHashMap.remove(gameId);
	}
	
	
	// TODO: use a separate thread to constantly save the gameData in a JSON file (persist life of application)
//	// Save game data to file
//    private ScheduledExecutorService scheduler =
//    	       Executors.newScheduledThreadPool(1);
//
//    	    public void saveGameData() {
//    	        final Runnable saver = new Runnable() {
//    	                public void run() {
//    	                	storeGameData();
//    	                }
//    	            };
//    	        
//    	        final ScheduledFuture<?> saverHandle =
//    	            scheduler.scheduleAtFixedRate(saver, 10, 10, TimeUnit.SECONDS);
//    	        
//    	        scheduler.schedule(new Runnable() {
//    	                public void run() { saverHandle.cancel(false); }
//    	            }, 60 * 60, TimeUnit.SECONDS);
//   }
//	
//	/**
//	 * Securely stores game data in persistent JSON file
//	 * 
//	 */
//	private void storeGameData() {
//		JSONArray allGamesJson = new JSONArray();
//		for (Game game : gamesHashMap.values()) {
//			allGamesJson.put(game.toJson());
//		}
//		
//		try {
//			FileWriter file = new FileWriter(GAME_DATASTORE_JSON_FILE);
//			file.write(allGamesJson.toString());
//			file.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("JSON file created: " + GAME_DATASTORE_JSON_FILE);
//	}

}
