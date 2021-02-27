package clueless;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/games")
class GameController {
	
	private int gameId = 1;
	private final HashMap<Integer, Game> gamesHashMap;

	GameController() {
		this.gamesHashMap = new HashMap<Integer, Game>();
	}

	/**
	 * Returns list of all active games
	 * @return
	 */
	@GetMapping()
	String getAllGames() {
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
		return this.gamesHashMap.get(gid).toString();
	}

	/**
	 * Deletes the provided game
	 * @param gid
	 */
	@DeleteMapping("/{gid}")
	void deleteGame(@PathVariable int gid) {
		this.gamesHashMap.remove(gid);
	}

	/**
	 * Returns list of players for a given game
	 * @return
	 */
	@GetMapping("/{gid}/players")
	String getAllPlayersInGame(@PathVariable int gid) {
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
		
		return this.toStringAllPlayersInGame(gid);
	}
	
	
	
	
	
	/**
	 * Returns string of all games
	 * @return
	 */
	String toStringAllGames() {
		String allGames = "Games: \n";
		for (Game game : this.gamesHashMap.values()) {
			allGames += "\t" + game.toString() + "\n";
		}
		return allGames;
	}
	
	/**
	 * Returns string of all players in a game
	 * @param gid
	 * @return
	 */
	String toStringAllPlayersInGame(int gid) {
		String allPlayers = "Players in Game " + gid + ": \n";
		for (Player player : this.gamesHashMap.get(gid).getPlayers().values()) {
			allPlayers += "\t" + player.toString() + "\n";
		}
		return allPlayers;
	}


}