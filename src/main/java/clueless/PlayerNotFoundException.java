package clueless;

@SuppressWarnings("serial")
public class PlayerNotFoundException extends RuntimeException {

	public PlayerNotFoundException(String charName) {
    super("Could not find player: " + charName);
  }
}