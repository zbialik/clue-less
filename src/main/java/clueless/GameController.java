package clueless;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GameController {

  private final GameRepository gameRepository;
  private final GameModelAssembler gameAssembler;

  GameController(GameRepository gameRepository, GameModelAssembler gameAssembler) {
    this.gameRepository = gameRepository;
    this.gameAssembler = gameAssembler;
  }
  
  /**
   * Returns list of all active games
   * @return
   */
  @GetMapping("/games")
  CollectionModel<EntityModel<Game>> all() {
    List<EntityModel<Game>> games = gameRepository.findAll().stream()
        .map(game -> EntityModel.of(game,
            linkTo(methodOn(GameController.class).one(game.getId())).withSelfRel(),
            linkTo(methodOn(GameController.class).all()).withRel("games")))
        .collect(Collectors.toList());

    return CollectionModel.of(games, linkTo(methodOn(GameController.class).all()).withSelfRel());
  }
  
  /**
   * Creates a new game object (starts new game)
   * @param newGame
   * @return
   */
  @PostMapping("/games")
  ResponseEntity<?> newGame(@RequestBody Game newGame) {

    EntityModel<Game> entityModel = gameAssembler.toModel(gameRepository.save(newGame));
    
    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }
  
  /**
   * Returns the specific game object requested
   * @param id
   * @return
   */
  @GetMapping("/games/{id}")
  EntityModel<Game> one(@PathVariable Long id) {

    Game game = gameRepository.findById(id) //
        .orElseThrow(() -> new GameNotFoundException(id));

    return EntityModel.of(game, //
        linkTo(methodOn(GameController.class).one(id)).withSelfRel(),
        linkTo(methodOn(GameController.class).all()).withRel("games"));
  }

  /**
   * Updates the game
   * @param newGame
   * @param id
   * @return
   */
  @PutMapping("/games/{id}")
  Game replaceGame(@RequestBody Game newGame, @PathVariable Long id) {
    
    return gameRepository.findById(id)
      .map(game -> {
        game.setPlayers(newGame.getPlayers());
        return gameRepository.save(game);
      })
      .orElseGet(() -> {
        newGame.setId(id);
        return gameRepository.save(newGame);
      });
  }

  /**
   * Deletes the provided game
   * @param id
   */
  @DeleteMapping("/games/{id}")
  void deleteGame(@PathVariable Long id) {
    gameRepository.deleteById(id);
  }
  
  
}