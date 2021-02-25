package clueless;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
class PlayerController {

	private final PlayerRepository playerRepository;
	private final PlayerModelAssembler playerAssembler;

	PlayerController(PlayerRepository playerRepository, PlayerModelAssembler playerAssembler) {
		this.playerRepository = playerRepository;
		this.playerAssembler = playerAssembler;
	}

	/**
	 * Returns list of all active players
	 * @return
	 */
	@GetMapping("/players")
	CollectionModel<EntityModel<Player>> all() {
		List<EntityModel<Player>> players = playerRepository.findAll().stream()
				.map(player -> EntityModel.of(player,
						linkTo(methodOn(PlayerController.class).one(player.getId())).withSelfRel(),
						linkTo(methodOn(PlayerController.class).all()).withRel("players")))
				.collect(Collectors.toList());

		return CollectionModel.of(players, linkTo(methodOn(PlayerController.class).all()).withSelfRel());
	}

	/**
	 * Creates a new player object (starts new player)
	 * @param newPlayer
	 * @return
	 */
	@PostMapping("/players")
	ResponseEntity<?> newPlayer(@RequestBody Player newPlayer) {

		EntityModel<Player> entityModel = playerAssembler.toModel(playerRepository.save(newPlayer));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	/**
	 * Returns the specific player object requested
	 * @param pid
	 * @return
	 */
	@GetMapping("/players/{pid}")
	EntityModel<Player> one(@PathVariable Long pid) {

		Player player = playerRepository.findById(pid) //
				.orElseThrow(() -> new PlayerNotFoundException(pid));

		return EntityModel.of(player, //
				linkTo(methodOn(PlayerController.class).one(pid)).withSelfRel(),
				linkTo(methodOn(PlayerController.class).all()).withRel("players"));
	}

	/**
	 * Deletes the provided player
	 * @param pid
	 */
	@DeleteMapping("/players/{pid}")
	void deletePlayer(@PathVariable Long pid) {
		playerRepository.deleteById(pid);
	}

//	TODO: fix
//	/**
//	 * Returns list of players for a given game
//	 * @return
//	 */
//	@GetMapping("/games/{gid}/players")
//	CollectionModel<EntityModel<Player>> allPlayersInGame(@PathVariable Long gid) {
//		
//		Game game = gameRepository.findById(gid) //
//				.orElseThrow(() -> new GameNotFoundException(gid));
//		
//		ArrayList<Player> currPlayers = game.getPlayers();
//		
//		CollectionModel<EntityModel<Player>> playersCollectionModel = playerAssembler.toCollectionModel(currPlayers);
//		
//		return playersCollectionModel;
//	}
}