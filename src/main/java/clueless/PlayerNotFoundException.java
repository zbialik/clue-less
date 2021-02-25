package clueless;

@SuppressWarnings("serial")
class PlayerNotFoundException extends RuntimeException {

	PlayerNotFoundException(Long id) {
    super("Could not find player: " + id);
  }
}