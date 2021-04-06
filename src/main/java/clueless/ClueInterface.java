package clueless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ClueInterface extends ClueConstants {
	
	// set ID lookup maps for characters and locations
	public static final Map<String, Integer> CHARACTER_ID_LOOKUP_MAP = initCharacterIdMap();
	public static final Map<String, Integer> LOCATION_ID_LOOKUP_MAP = initLocationIdMap();

	// set maps using ID's defined in CHARACTER_ID_LOOKUP_MAP and LOCATION_ID_LOOKUP_MAP
	public static final Map<Integer, Card> CARD_ID_MAP = initCardMap();
	public static final Map<Integer, Character> CHARACTER_ID_MAP = initCharacterMap();
	public static final Map<Integer, Location> LOCATION_ID_MAP = initLocationMap();

	public static Map<String, Integer> initCharacterIdMap() {
		Map<String, Integer> map = new HashMap<>();

		// add characters
		map.put(CHARACTER_NAME_MRS_WHITE, 1);
		map.put(CHARACTER_NAME_MR_GREEN, 2);
		map.put(CHARACTER_NAME_MRS_PEACOCK, 3);
		map.put(CHARACTER_NAME_PROF_PLUM, 4);
		map.put(CHARACTER_NAME_MISS_SCARLET, 5);
		map.put(CHARACTER_NAME_COLONEL_MUSTARD, 6);

		return Collections.unmodifiableMap(map);
	}

	public static Map<String, Integer> initLocationIdMap() {
		Map<String, Integer> map = new HashMap<>();

		// add rooms
		map.put(LOCATION_NAME_BALL_ROOM, 1);
		map.put(LOCATION_NAME_BILLIARD_ROOM, 2);
		map.put(LOCATION_NAME_CONSERVATORY, 3);
		map.put(LOCATION_NAME_DINING_ROOM, 4);
		map.put(LOCATION_NAME_HALL, 5);
		map.put(LOCATION_NAME_KITCHEN, 6);
		map.put(LOCATION_NAME_LOUNGE, 7);
		map.put(LOCATION_NAME_LIBRARY, 8);
		map.put(LOCATION_NAME_STUDY, 9);

		// add homes
		map.put(LOCATION_NAME_MRS_WHITE_HOME, 10);
		map.put(LOCATION_NAME_MR_GREEN_HOME, 11);
		map.put(LOCATION_NAME_MRS_PEACOCK_HOME, 12);
		map.put(LOCATION_NAME_PROF_PLUM_HOME, 13);
		map.put(LOCATION_NAME_MISS_SCARLET_HOME, 14);
		map.put(LOCATION_NAME_COLONEL_MUSTARD_HOME, 15);

		// add hallways
		map.put(LOCATION_NAME_HALLWAY_32, 16);
		map.put(LOCATION_NAME_HALLWAY_52, 17);
		map.put(LOCATION_NAME_HALLWAY_23, 18);
		map.put(LOCATION_NAME_HALLWAY_43, 19);
		map.put(LOCATION_NAME_HALLWAY_63, 20);
		map.put(LOCATION_NAME_HALLWAY_34, 21);
		map.put(LOCATION_NAME_HALLWAY_54, 22);
		map.put(LOCATION_NAME_HALLWAY_25, 23);
		map.put(LOCATION_NAME_HALLWAY_45, 24);
		map.put(LOCATION_NAME_HALLWAY_65, 25);
		map.put(LOCATION_NAME_HALLWAY_36, 26);
		map.put(LOCATION_NAME_HALLWAY_56, 27);

		return Collections.unmodifiableMap(map);
	}

	public static Map<Integer, Card> initCardMap() {
		Map<Integer, Card> map = new HashMap<>();

		// add weapon cards
		map.put(1, new Card(1, WEAPON_NAME_KNIFE, CARD_TYPE_WEAPON));
		map.put(2, new Card(2, WEAPON_NAME_REVOLVER, CARD_TYPE_WEAPON));
		map.put(3, new Card(3, WEAPON_NAME_ROPE, CARD_TYPE_WEAPON));
		map.put(4, new Card(4, WEAPON_NAME_WRENCH, CARD_TYPE_WEAPON));
		map.put(5, new Card(5, WEAPON_NAME_CANDLESTICK, CARD_TYPE_WEAPON));
		map.put(6, new Card(6, WEAPON_NAME_LEADPIPE, CARD_TYPE_WEAPON));

		// add room cards
		map.put(7, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BALL_ROOM), 7, LOCATION_NAME_BALL_ROOM));
		map.put(8, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BILLIARD_ROOM), 8, LOCATION_NAME_BILLIARD_ROOM));
		map.put(9, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_CONSERVATORY), 9, LOCATION_NAME_CONSERVATORY));
		map.put(10, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_DINING_ROOM), 10, LOCATION_NAME_DINING_ROOM));
		map.put(11, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALL), 11, LOCATION_NAME_HALL));
		map.put(12, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_KITCHEN), 12, LOCATION_NAME_KITCHEN));
		map.put(13, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LOUNGE), 13, LOCATION_NAME_LOUNGE));
		map.put(14, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LIBRARY), 14, LOCATION_NAME_LIBRARY));
		map.put(15, new RoomCard(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_STUDY), 15, LOCATION_NAME_STUDY));

		// add suspect cards
		map.put(16, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_WHITE_HOME), 16, LOCATION_NAME_MRS_WHITE_HOME));
		map.put(17, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_MR_GREEN_HOME), 17, LOCATION_NAME_MR_GREEN_HOME));
		map.put(18, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_PEACOCK_HOME), 18, LOCATION_NAME_MRS_PEACOCK_HOME));
		map.put(19, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_PROF_PLUM_HOME), 19, LOCATION_NAME_PROF_PLUM_HOME));
		map.put(20, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_MISS_SCARLET_HOME), 20, LOCATION_NAME_MISS_SCARLET_HOME));
		map.put(21, new SuspectCard(CHARACTER_ID_LOOKUP_MAP.get(LOCATION_NAME_COLONEL_MUSTARD_HOME), 21, LOCATION_NAME_COLONEL_MUSTARD_HOME));

		return Collections.unmodifiableMap(map);
	}

	public static Map<Integer, Location> initLocationMap() { 
		Map<Integer, Location> map = new HashMap<>();

		// add rooms
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BALL_ROOM), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BALL_ROOM), LOCATION_TYPE_ROOM, 4, 2 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BILLIARD_ROOM), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_BILLIARD_ROOM), LOCATION_TYPE_ROOM, 4, 4 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_CONSERVATORY), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_CONSERVATORY), LOCATION_TYPE_ROOM, 2, 2 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_DINING_ROOM), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_DINING_ROOM), LOCATION_TYPE_ROOM, 6, 4 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALL), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALL), LOCATION_TYPE_ROOM, 4, 6 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_KITCHEN), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_KITCHEN), LOCATION_TYPE_ROOM, 6, 2 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LOUNGE), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LOUNGE), LOCATION_TYPE_ROOM, 6, 6 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LIBRARY), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_LIBRARY), LOCATION_TYPE_ROOM, 2, 4 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_STUDY), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_STUDY), LOCATION_TYPE_ROOM, 2, 6 ));

		// add homes
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_WHITE_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_WHITE_HOME), LOCATION_TYPE_HOME, 5, 1 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MR_GREEN_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MR_GREEN_HOME), LOCATION_TYPE_HOME, 3, 1 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_PEACOCK_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_PEACOCK_HOME), LOCATION_TYPE_HOME, 1, 3 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_PROF_PLUM_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_PROF_PLUM_HOME), LOCATION_TYPE_HOME, 1, 5 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MISS_SCARLET_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MISS_SCARLET_HOME), LOCATION_TYPE_HOME, 5, 7 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_COLONEL_MUSTARD_HOME), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_COLONEL_MUSTARD_HOME), LOCATION_TYPE_HOME, 7, 5 ));

		// add hallways
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_32), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_32), LOCATION_TYPE_HOME, 3, 2 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_52), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_52), LOCATION_TYPE_HOME, 5, 2 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_23), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_23), LOCATION_TYPE_HOME, 2, 3 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_43), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_43), LOCATION_TYPE_HOME, 4, 3 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_63), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_63), LOCATION_TYPE_HOME, 6, 3 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_34), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_34), LOCATION_TYPE_HOME, 3, 4 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_54), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_54), LOCATION_TYPE_HOME, 5, 4 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_25), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_25), LOCATION_TYPE_HOME, 2, 5 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_45), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_45), LOCATION_TYPE_HOME, 4, 5 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_65), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_65), LOCATION_TYPE_HOME, 6, 5 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_36), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_36), LOCATION_TYPE_HOME, 3, 6 ));
		map.put(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_56), new Location(
				LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_HALLWAY_56), LOCATION_TYPE_HOME, 5, 6 ));

		return Collections.unmodifiableMap(map);
	}

	public static Map<Integer, Character> initCharacterMap() { 
		Map<Integer, Character> map = new HashMap<>();
		
		// add characters
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MRS_WHITE), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MRS_WHITE), 
						CHARACTER_NAME_MRS_WHITE, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_WHITE_HOME))));
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MR_GREEN), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MR_GREEN), 
						CHARACTER_NAME_MR_GREEN, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MR_GREEN_HOME))));
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MRS_PEACOCK), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MRS_PEACOCK), 
						CHARACTER_NAME_MRS_PEACOCK, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MRS_PEACOCK_HOME))));
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_PROF_PLUM), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_PROF_PLUM), 
						CHARACTER_NAME_PROF_PLUM, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_PROF_PLUM_HOME))));
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MISS_SCARLET), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_MISS_SCARLET), 
						CHARACTER_NAME_MISS_SCARLET, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_MISS_SCARLET_HOME))));
		map.put(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_COLONEL_MUSTARD), 
				new Character(CHARACTER_ID_LOOKUP_MAP.get(CHARACTER_NAME_COLONEL_MUSTARD), 
						CHARACTER_NAME_COLONEL_MUSTARD, 
						LOCATION_ID_MAP.get(LOCATION_ID_LOOKUP_MAP.get(LOCATION_NAME_COLONEL_MUSTARD_HOME))));
		
		return Collections.unmodifiableMap(map);
	}

	/**
	 * Returns the instance of the card class
	 * @param cardId
	 * @return
	 */
	public default Card identifyCard(int cardId) {

		if (CARD_ID_MAP.get(cardId) instanceof SuspectCard) {
			return (SuspectCard) CARD_ID_MAP.get(cardId);
		} else if (CARD_ID_MAP.get(cardId) instanceof RoomCard) {
			return (RoomCard) CARD_ID_MAP.get(cardId);
		} else {
			return CARD_ID_MAP.get(cardId);
		}
	}
	
	/**
	 * Returns a list of card objects in random order
	 * @return cardDeck
	 */
	public static List<Card> getShuffledDeck() {
		
		List<Card> cardDeck = new ArrayList<Card>();

		for (Card card : CARD_ID_MAP.values()) {
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
		// TODO: fill with logic (use getShuffledDeck() helper for support)
	}
	
	/**
	 * Returns a JSONArray representing the list of cards provided
	 * @return cardsListJson
	 */
	public default JSONArray cardsToJsonArray(List<Card> cards) {
		
		JSONArray cardsJsonArray = new JSONArray();
		
		for (Card card : cards) {
			if ( card instanceof RoomCard ) {
				cardsJsonArray.put(( (RoomCard) card ).toJson());
			} else if ( card instanceof SuspectCard ) {
				cardsJsonArray.put(( (SuspectCard) card ).toJson());
			} else {
				cardsJsonArray.put(card.toJson());
			}
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
	public default JSONObject charMapToJsonObject(Map<Integer, Character> characterMap) {
		
		JSONObject charMapObject = new JSONObject();
		
		for (int characterId : characterMap.keySet()) {
			charMapObject.append(String.valueOf(characterId), characterMap.get(characterId).toJson());
		}
		
		return charMapObject;
		
	}

}
