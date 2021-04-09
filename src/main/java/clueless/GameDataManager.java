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
import org.json.JSONArray;

public class GameDataManager implements ClueInterface {

	static final Logger LOGGER = LogManager.getLogger(GameService.class);
	
	static final String GAME_DATASTORE_JSON_FILE = System.getProperty("user.dir") + "/gameDataStore.json";

	// game datastore
	public HashMap<Integer, Game> gamesHashMap = new HashMap<Integer, Game>();
	private int gameIdCounter = 1;
	
	// TODO: (ZACH) insert section for reading JSON file if it exists and reloading game datastore

	/**
	 * Returns json array of all games
	 * @return allGamesJson
	 */
	public JSONArray getAllGamesJSON() {
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
	public Game getGame(int gid) {
		return gamesHashMap.get(gid);
	}

	/**
	 * Adds new game to datastore and returns the game created
	 * @param charName
	 * @param name
	 * @return newGameJson
	 */
	public synchronized Game addNewGame(String charName, String name) {

		Game newGame = new Game(this.gameIdCounter, INIT_CHARACTER_MAP);
		
		newGame.addPlayer(charName, name, INIT_CHARACTER_MAP.get(charName).characterHome); // include player in initialized game
		
		LOGGER.info(name + " initialized Game " + this.gameIdCounter + ".");
		gamesHashMap.put(this.gameIdCounter, newGame); // initialize new game
		
		this.gameIdCounter++; // increment game ID to set unique ID's for games

		return newGame;
	}

	public void deleteGame(int gameId) {
		gamesHashMap.remove(gameId);
		LOGGER.info("Game " + gameId + " deleted.");
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
