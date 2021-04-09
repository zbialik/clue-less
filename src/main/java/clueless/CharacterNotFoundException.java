package clueless;

@SuppressWarnings("serial")
public class CharacterNotFoundException extends RuntimeException {

	public CharacterNotFoundException(String charName) {
    super("Could not find character: " + charName);
  }
}