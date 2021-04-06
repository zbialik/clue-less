package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import org.json.JSONObject;

@SuppressWarnings("serial")
@Entity
/**
 * The Player class represents a unique player for a clue-less game.
 * 
 * @author Zach Bialik
 */
public class Player extends Character implements Serializable, ClueInterface {

	public String playerName;
	public String state;
	public boolean vip = false;
	private List<Card> handCards = new ArrayList<Card>();
	private List<Card> knownCards = new ArrayList<Card>();
	private Card revealedClueCard = null;
	private List<Location> possibleMoves = new ArrayList<Location>();
	private String eventMessage = new String();

	public Player(int charId, String name) { // unique constructor
		super(charId, name, CHARACTER_ID_MAP.get(charId).characterHome);
		this.playerName = null;
		this.state = PLAYER_STATE_WAIT;
	}
	
	public Player(int charId, String name, boolean firstPlayer) { // unique constructor
		super(charId, name, CHARACTER_ID_MAP.get(charId).characterHome);
		this.playerName = null;
		this.state = PLAYER_STATE_WAIT;
		this.vip = firstPlayer;
	}
	
	/**
	 * Returns the list of cards in this player's hand
	 * @return
	 */
	public List<Card> getHand() {
		return this.handCards;
	}
	
	/**
	 * Returns true if this player's hand has a card in the provided suggestion
	 * @return
	 */
	public boolean hasClue(List<Card> suggestion) {
		
		boolean hasClue = false;
		
		// TODO: verify works correctly
		for (Card suggestedCard : suggestion) { 
			for (Card handCard : this.handCards) { 
				if (suggestedCard.equals(handCard)) {
					return true;
				}
			}
		}
		
		return hasClue;
	}
	
	/**
	 * Clears this player's list of possible moves
	 * @return
	 */
	public void clearPossibleMoves() {
		this.possibleMoves.clear();
	}
	
	/**
	 * Clears this player's revealedClueCard
	 * @return
	 */
	public void clearRevealedClueCard() {
		this.revealedClueCard = null;
	}
	
	/**
	 * Sets this player's revealedClueCard
	 * @param c
	 * @return
	 */
	public void setRevealedClueCard(Card c) {
		this.revealedClueCard = c;
	}
	
	/**
	 * Sets this player's revealedClueCard
	 * @return
	 */
	public Card getRevealedClueCard() {
		return this.revealedClueCard;
	}
	
	/**
	 * Sets this player's possible moves list
	 * param l
	 * @return
	 */
	public void setPossibleMoves(List<Location> l) {
		this.possibleMoves = l;
	}
	
	/**
	 * Gets this player's possible moves list
	 * @return
	 */
	public List<Location> getPossibleMoves() {
		return this.possibleMoves;
	}
	
	/**
	 * Sets this player's event message
	 * param l
	 * @return
	 */
	public void setEventMessage(String event) {
		this.eventMessage = event;
	}
	
	/**
	 * Gets this player's event message
	 * @return
	 */
	public String getEventMessage() {
		return this.eventMessage;
	}
	
	/**
	 * Adds a card to this player's knownCards list
	 * @return
	 */
	public void addKnownCard(Card c) {
		this.knownCards.add(c);
	}
	
	/**
	 * Returns JSONObject representation for this player
	 */
	public JSONObject toJson() {
		
		JSONObject playerJson = super.toJson();
		playerJson.put("playerName", this.playerName); 
		playerJson.put("state", this.state); 
		playerJson.put("vip", this.vip);
		playerJson.put("handCards", cardsToJsonArray(this.handCards)); 
		playerJson.put("knownCards", cardsToJsonArray(this.knownCards)); 
		playerJson.put("revealedClueCard", this.revealedClueCard.toJson());
		playerJson.put("possibleMoves", locationsToJsonArray(this.possibleMoves)); 
		playerJson.put("eventMessage", this.eventMessage); 
		
		return playerJson;
	}
	
	// TODO: delete if OBE
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (!(o instanceof Player))
//			return false;
//		Player playerO = (Player) o;
//		return Objects.equals(this.characterId, playerO.characterId) 
//				&& Objects.equals(this.playerName, playerO.playerName) 
//				&& Objects.equals(this.characterName, playerO.characterName);
//	}
	
	
	// TODO: delete if OBE
//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (!(o instanceof Player))
//			return false;
//		Player playerO = (Player) o;
//		return Objects.equals(this.characterId, playerO.characterId) 
//				&& Objects.equals(this.playerName, playerO.playerName) 
//				&& Objects.equals(this.characterName, playerO.characterName);
//	}
	
}