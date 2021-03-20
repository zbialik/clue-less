package clueless;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

/**
 * This class will control all in-game logic to ensure the required Clue game rules
 * are abided by. Additionally, this class will validate all requests for updating
 * and retrieving data from the datastore.
 * @author zachbialik
 *
 */
public class GameController implements GameDataManager {
	
	// Save game data to file
//    private final ScheduledExecutorService scheduler =
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
//    	    }
	
	/**
	 * Securely stores game data in persistent JSON file
	 * 
	 */
	private static void storeGameData() {
		JSONArray allGamesJson = new JSONArray();
		for (Game game : gamesHashMap.values()) {
			allGamesJson.put(game.toJson());
		}
		
		try {
			FileWriter file = new FileWriter(GAME_DATASTORE_JSON_FILE);
			file.write(allGamesJson.toString());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("JSON file created: " + GAME_DATASTORE_JSON_FILE);
	}
}
