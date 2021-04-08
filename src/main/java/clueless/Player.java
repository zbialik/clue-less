package clueless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

@SuppressWarnings("serial")
/**
 * The Player class represents a unique player for a clue-less game.
 * 
 * @author Zach Bialik
 */
public class Player extends Character implements Serializable {

	public String playerName;
	public String state;
	public boolean vip = false;
	private List<Card> handCards = new ArrayList<Card>();
	private List<Card> knownCards = new ArrayList<Card>();
	private Card revealedClueCard = null;
	private List<Location> possibleMoves = new ArrayList<Location>();
	private String eventMessage = new String();

	public Player(String charName, String name, Location location) { // unique constructor
		super(charName, location); 
		this.activate(); // always change character to active when player is instantiated
		this.playerName = name;
		this.state = PLAYER_STATE_WAIT;
	}
	
	public Player(String charName, String name, boolean firstPlayer, Location location) { // unique constructor
		super(charName, location);
		this.activate(); // always change character to active when player is instantiated
		this.playerName = name;
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
	 * Adds a card to this player's hand list and also updates knownCards
	 * @return
	 */
	public void addHandCard(Card c) {
		this.handCards.add(c);
		this.knownCards.add(c);
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
		if (this.revealedClueCard == null) {
			playerJson.put("revealedClueCard", "null"); // just put ID of secret passage for JSON
		} else {
			playerJson.put("revealedClueCard", this.revealedClueCard.toJson()); // just put ID of secret passage for JSON
		}
		playerJson.put("possibleMoves", locationsToJsonArray(this.possibleMoves)); 
		playerJson.put("eventMessage", this.eventMessage); 
		
		return playerJson;
	}
	
}