package clueless;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {

  @Override
  public EntityModel<Player> toModel(Player player) {

    return EntityModel.of(player, //
        linkTo(methodOn(PlayerController.class).one(player.getId())).withSelfRel(),
        linkTo(methodOn(PlayerController.class).all()).withRel("players"));
  }
}