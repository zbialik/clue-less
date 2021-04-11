package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public interface ClueInterface extends ClueConstants {
	
//	public static final Map<String, Character> INIT_CHARACTER_MAP = initCharacterMap();
	
	public static final Map<String, Location> LOCATION_MAP = initLocationMap();
	public static final Map<String, Card> CARD_MAP = initCardMap();
	public static final String[] CHARACTER_TURN_ORDER = { // use this to determine order of turns
			CHARACTER_NAME_MISS_SCARLET,
			CHARACTER_NAME_COLONEL_MUSTARD,
			CHARACTER_NAME_MRS_WHITE,
			CHARACTER_NAME_MR_GREEN,
			CHARACTER_NAME_MRS_PEACOCK,
			CHARACTER_NAME_PROF_PLUM
	};

	public static Map<String, Card> initCardMap() {
		Map<String, Card> map = new HashMap<>();

		// add weapon cards
		map.put(WEAPON_NAME_KNIFE, new Card(WEAPON_NAME_KNIFE, CARD_TYPE_WEAPON));
		map.put(WEAPON_NAME_REVOLVER, new Card(WEAPON_NAME_REVOLVER, CARD_TYPE_WEAPON));
		map.put(WEAPON_NAME_ROPE, new Card(WEAPON_NAME_ROPE, CARD_TYPE_WEAPON));
		map.put(WEAPON_NAME_WRENCH, new Card(WEAPON_NAME_WRENCH, CARD_TYPE_WEAPON));
		map.put(WEAPON_NAME_CANDLESTICK, new Card(WEAPON_NAME_CANDLESTICK, CARD_TYPE_WEAPON));
		map.put(WEAPON_NAME_LEADPIPE, new Card(WEAPON_NAME_LEADPIPE, CARD_TYPE_WEAPON));

		// add room cards
		map.put(LOCATION_NAME_BALL_ROOM, new Card(LOCATION_NAME_BALL_ROOM, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_BILLIARD_ROOM, new Card(LOCATION_NAME_BILLIARD_ROOM, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_CONSERVATORY, new Card(LOCATION_NAME_CONSERVATORY, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_DINING_ROOM, new Card(LOCATION_NAME_DINING_ROOM, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_HALL, new Card(LOCATION_NAME_HALL, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_KITCHEN, new Card(LOCATION_NAME_KITCHEN, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_LOUNGE, new Card(LOCATION_NAME_LOUNGE, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_LIBRARY, new Card(LOCATION_NAME_LIBRARY, CARD_TYPE_ROOM));
		map.put(LOCATION_NAME_STUDY, new Card(LOCATION_NAME_STUDY, CARD_TYPE_ROOM));

		// add suspect cards
		map.put(CHARACTER_NAME_MRS_WHITE, new Card(CHARACTER_NAME_MRS_WHITE, CARD_TYPE_SUSPECT));
		map.put(CHARACTER_NAME_MR_GREEN, new Card(CHARACTER_NAME_MR_GREEN, CARD_TYPE_SUSPECT));
		map.put(CHARACTER_NAME_MRS_PEACOCK, new Card(CHARACTER_NAME_MRS_PEACOCK, CARD_TYPE_SUSPECT));
		map.put(CHARACTER_NAME_PROF_PLUM, new Card(CHARACTER_NAME_PROF_PLUM, CARD_TYPE_SUSPECT));
		map.put(CHARACTER_NAME_MISS_SCARLET, new Card(CHARACTER_NAME_MISS_SCARLET, CARD_TYPE_SUSPECT));
		map.put(CHARACTER_NAME_COLONEL_MUSTARD, new Card(CHARACTER_NAME_COLONEL_MUSTARD, CARD_TYPE_SUSPECT));

		return map;
	}

	public static Map<String, Location> initLocationMap() { 
		Map<String, Location> map = new HashMap<>();

		// add rooms
		map.put(LOCATION_NAME_BALL_ROOM, new Location(
				LOCATION_NAME_BALL_ROOM, LOCATION_TYPE_ROOM, 4, 2 ));
		map.put(LOCATION_NAME_BILLIARD_ROOM, new Location(
				LOCATION_NAME_BILLIARD_ROOM, LOCATION_TYPE_ROOM, 4, 4 ));
		map.put(LOCATION_NAME_CONSERVATORY, new Location(
				LOCATION_NAME_CONSERVATORY, LOCATION_TYPE_ROOM, 2, 2 ));
		map.put(LOCATION_NAME_DINING_ROOM, new Location(
				LOCATION_NAME_DINING_ROOM, LOCATION_TYPE_ROOM, 6, 4 ));
		map.put(LOCATION_NAME_HALL, new Location(
				LOCATION_NAME_HALL, LOCATION_TYPE_ROOM, 4, 6 ));
		map.put(LOCATION_NAME_KITCHEN, new Location(
				LOCATION_NAME_KITCHEN, LOCATION_TYPE_ROOM, 6, 2 ));
		map.put(LOCATION_NAME_LOUNGE, new Location(
				LOCATION_NAME_LOUNGE, LOCATION_TYPE_ROOM, 6, 6 ));
		map.put(LOCATION_NAME_LIBRARY, new Location(
				LOCATION_NAME_LIBRARY, LOCATION_TYPE_ROOM, 2, 4 ));
		map.put(LOCATION_NAME_STUDY, new Location(
				LOCATION_NAME_STUDY, LOCATION_TYPE_ROOM, 2, 6 ));
		
		// update rooms with secretPassages
		map.get(LOCATION_NAME_LOUNGE).secretPassage = map.get(LOCATION_NAME_CONSERVATORY);
		map.get(LOCATION_NAME_CONSERVATORY).secretPassage = map.get(LOCATION_NAME_LOUNGE);
		map.get(LOCATION_NAME_KITCHEN).secretPassage = map.get(LOCATION_NAME_STUDY);
		map.get(LOCATION_NAME_STUDY).secretPassage = map.get(LOCATION_NAME_KITCHEN);

		// add homes
		map.put(LOCATION_NAME_MRS_WHITE_HOME, new Location(
				LOCATION_NAME_MRS_WHITE_HOME, LOCATION_TYPE_HOME, 5, 1 ));
		map.put(LOCATION_NAME_MR_GREEN_HOME, new Location(
				LOCATION_NAME_MR_GREEN_HOME, LOCATION_TYPE_HOME, 3, 1 ));
		map.put(LOCATION_NAME_MRS_PEACOCK_HOME, new Location(
				LOCATION_NAME_MRS_PEACOCK_HOME, LOCATION_TYPE_HOME, 1, 3 ));
		map.put(LOCATION_NAME_PROF_PLUM_HOME, new Location(
				LOCATION_NAME_PROF_PLUM_HOME, LOCATION_TYPE_HOME, 1, 5 ));
		map.put(LOCATION_NAME_MISS_SCARLET_HOME, new Location(
				LOCATION_NAME_MISS_SCARLET_HOME, LOCATION_TYPE_HOME, 5, 7 ));
		map.put(LOCATION_NAME_COLONEL_MUSTARD_HOME, new Location(
				LOCATION_NAME_COLONEL_MUSTARD_HOME, LOCATION_TYPE_HOME, 7, 5 ));

		// add hallways
		map.put(LOCATION_NAME_HALLWAY_32, new Location(
				LOCATION_NAME_HALLWAY_32, LOCATION_TYPE_HALLWAY, 3, 2 ));
		map.put(LOCATION_NAME_HALLWAY_52, new Location(
				LOCATION_NAME_HALLWAY_52, LOCATION_TYPE_HALLWAY, 5, 2 ));
		map.put(LOCATION_NAME_HALLWAY_23, new Location(
				LOCATION_NAME_HALLWAY_23, LOCATION_TYPE_HALLWAY, 2, 3 ));
		map.put(LOCATION_NAME_HALLWAY_43, new Location(
				LOCATION_NAME_HALLWAY_43, LOCATION_TYPE_HALLWAY, 4, 3 ));
		map.put(LOCATION_NAME_HALLWAY_63, new Location(
				LOCATION_NAME_HALLWAY_63, LOCATION_TYPE_HALLWAY, 6, 3 ));
		map.put(LOCATION_NAME_HALLWAY_34, new Location(
				LOCATION_NAME_HALLWAY_34, LOCATION_TYPE_HALLWAY, 3, 4 ));
		map.put(LOCATION_NAME_HALLWAY_54, new Location(
				LOCATION_NAME_HALLWAY_54, LOCATION_TYPE_HALLWAY, 5, 4 ));
		map.put(LOCATION_NAME_HALLWAY_25, new Location(
				LOCATION_NAME_HALLWAY_25, LOCATION_TYPE_HALLWAY, 2, 5 ));
		map.put(LOCATION_NAME_HALLWAY_45, new Location(
				LOCATION_NAME_HALLWAY_45, LOCATION_TYPE_HALLWAY, 4, 5 ));
		map.put(LOCATION_NAME_HALLWAY_65, new Location(
				LOCATION_NAME_HALLWAY_65, LOCATION_TYPE_HALLWAY, 6, 5 ));
		map.put(LOCATION_NAME_HALLWAY_36, new Location(
				LOCATION_NAME_HALLWAY_36, LOCATION_TYPE_HALLWAY, 3, 6 ));
		map.put(LOCATION_NAME_HALLWAY_56, new Location(
				LOCATION_NAME_HALLWAY_56, LOCATION_TYPE_HALLWAY, 5, 6 ));

		return map;
	}

	public default Map<String, Character> initCharacterMap() { 
		Map<String, Character> map = new HashMap<String, Character>();
		Map<String, Location>  locMap = initLocationMap();
		
		// add characters
		map.put(CHARACTER_NAME_MRS_WHITE,
				new Character(CHARACTER_NAME_MRS_WHITE, 
						locMap.get(LOCATION_NAME_MRS_WHITE_HOME)));
		map.put(CHARACTER_NAME_MR_GREEN, 
				new Character(CHARACTER_NAME_MR_GREEN, 
						locMap.get(LOCATION_NAME_MR_GREEN_HOME)));
		map.put(CHARACTER_NAME_MRS_PEACOCK, 
				new Character(CHARACTER_NAME_MRS_PEACOCK, 
						locMap.get(LOCATION_NAME_MRS_PEACOCK_HOME)));
		map.put(CHARACTER_NAME_PROF_PLUM, 
				new Character(CHARACTER_NAME_PROF_PLUM, 
						locMap.get(LOCATION_NAME_PROF_PLUM_HOME)));
		map.put(CHARACTER_NAME_MISS_SCARLET, 
				new Character(CHARACTER_NAME_MISS_SCARLET, 
						locMap.get(LOCATION_NAME_MISS_SCARLET_HOME)));
		map.put(CHARACTER_NAME_COLONEL_MUSTARD, 
				new Character(CHARACTER_NAME_COLONEL_MUSTARD, 
						locMap.get(LOCATION_NAME_COLONEL_MUSTARD_HOME)));
		
		return map;
	}
	
	/**
	 * Returns a list of locations that are adjacent to the provided location
	 * @return adjacentLocations
	 */
	public default List<Location> getAdjacentLocations(Location loc) {
		
		List<Location> adjacentLocations = new ArrayList<Location>();

		for (Location locInMap : LOCATION_MAP.values()) {
			if (loc.isAdjacent(locInMap)) {
				adjacentLocations.add(locInMap);
			}
		}
		
		return adjacentLocations;
	}
	
	/**
	 * Returns the index in CHARACTER_TURN_ORDER that a character is in
	 * @return characterHome
	 */
	public default int getCharacterIndexInTurnOrder(String charName) {
		
		for (int i = 0; i < CHARACTER_TURN_ORDER.length; i++) {
			if (CHARACTER_TURN_ORDER[i].equals(charName)) {
				return i;
			}
		}
		
		return CHARACTER_TURN_ORDER.length; // return out of range integer
	}
	
	/**
	 * Returns a list of card objects in random order
	 * @return cardDeck
	 */
	public static List<Card> getShuffledDeck() {
		
		List<Card> cardDeck = new ArrayList<Card>();

		for (Card card : CARD_MAP.values()) {
            cardDeck.add(card);
		}
		
		Collections.shuffle(cardDeck); // shuffle deck and return
		
		return cardDeck;
	}
	
	/**
	 * Deals cards for the game provided
	 * @return cardDeck
	 */
	public default void dealCards(Game game) {
		
		// get shuffled deck
		List<Card> cardDeck = getShuffledDeck();
		
		// add suspect card to mysteryCards in game
		addMysteryCard(game, cardDeck, CARD_TYPE_SUSPECT);
		
		// add room card to mysteryCards in game
		addMysteryCard(game, cardDeck, CARD_TYPE_ROOM);
		
		// add weapon card to mysteryCards in game
		addMysteryCard(game, cardDeck, CARD_TYPE_WEAPON);
		
		// distribute remaining cards to each player
		dealPlayersCards(game, cardDeck);
		
	}
	
	/**
	 * Returns the remaining shuffled deck after updating the game's mysteryCards with one
	 * randomly selected card matching the provided cardType
	 * @param game
	 * @param shuffledDeck
	 * @param cardType
	 */
	public static void addMysteryCard(Game game, List<Card> shuffledDeck, String cardType) {
		boolean foundCard = false;
		
		int i = 0;
		while (!foundCard) {
			if (shuffledDeck.get(i).type == cardType) {
				game.mysteryCards.add(shuffledDeck.get(i)); // add to mystery deck
				shuffledDeck.remove(i); // remove from cardDeck to distribute
				foundCard = true;
			}
			i++;
			
			if (i >= shuffledDeck.size()) {
				throw new RuntimeException("could not find card in cardDeck that is of type: " + cardType);
			}
		}
		foundCard = true;
	}
	
	/**
	 * Returns the remaining shuffled deck after updating the game's mysteryCards with one
	 * randomly selected card matching the provided cardType
	 * @param game
	 * @param shuffledDeck
	 */
	public static void dealPlayersCards(Game game, List<Card> shuffledDeck) {
		
		// get list of players in the game
		List<Player> players = new ArrayList<Player>();
		for (Character character : game.characterMap.values()) {
			if (character instanceof Player) {
				players.add((Player) character);
			}
		}
		
		int j = 0;
		while (shuffledDeck.size() >= 1) {
			
			// add card to player hand
			players.get(j).addHandCard(shuffledDeck.get(0));
			
			// remove card from shuffled deck
			shuffledDeck.remove(0);
			
			// point to next player
			j++;
			if (j >= players.size()) { // reset to first player in list
				j = 0;
			}
		}
		
	}
	
	/**
	 * Returns a JSONArray representing the list of cards provided
	 * @return cardsListJson
	 */
	public default JSONArray cardsToJsonArray(List<Card> cards) {
		
		JSONArray cardsJsonArray = new JSONArray();
		
		for (Card card : cards) {
			cardsJsonArray.put(card.toJson());
		}
		
		return cardsJsonArray;
		
	}
	
	/**
	 * Returns a JSONArray representing the list of locations provided
	 * @return loationsListJson
	 */
	public default JSONArray locationsToJsonArray(List<Location> locations) {
		
		JSONArray loationsListJson = new JSONArray();
		
		for (Location location : locations) {
			loationsListJson.put(location.toJson());
		}
		
		return loationsListJson;
		
	}
	
	/**
	 * Returns a JSONArray representing the list of players provided
	 * @return playersJsonArray
	 */
	public default JSONArray playersToJsonArray(List<Player> players) {
		
		JSONArray playersJsonArray = new JSONArray();
		
		for (Player player : players) {
			playersJsonArray.put(player.toJson());
		}
		
		return playersJsonArray;
		
	}
	
	/**
	 * Returns a JSONObject representing the character map provided
	 * @return loationsListJson
	 */
	public default JSONObject charMapToJsonObject(Map<String, Character> characterMap) {
		
		JSONObject charMapObject = new JSONObject();
		
		for (Character character : characterMap.values()) {
			charMapObject.put(character.characterName, character.toJson());
//			charMapObject.put(String.valueOf(character.characterId), character.toJson()); TODO: delete from testing
		}
		
		return charMapObject;
		
	}

}
