package clueless;

@SuppressWarnings("serial")
public class PlayerNotFoundException extends RuntimeException {

	public PlayerNotFoundException(int pid) {
    super("Could not find player: " + pid);
  }
}