package clueless;

@SuppressWarnings("serial")
class GameNotFoundException extends RuntimeException {

	GameNotFoundException(Long id) {
    super("Could not find game: " + id);
  }
}