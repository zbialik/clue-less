package clueless;

import java.util.Objects;

import org.json.JSONObject;

/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Card {
	
	public int cardId;
	public String name;
	public String type;
	public boolean isWinner;
	
	Card(int id, String n, boolean w, String tp) { // constructor
		this.cardId = id;
		this.name = n;
		this.isWinner = w;
		this.type = tp;
	}
	
	Card(int id, String n, String tp) { // if boolean not provided, assume false
		this.cardId = id;
		this.name = n;
		this.isWinner = false;
		this.type = tp;
	}
	
	/**
	 * Returns information relevant to the card as json
	 */
	public JSONObject toJson() {
		
		JSONObject cardJson = new JSONObject();
		cardJson.put("cardId",this.cardId);
		cardJson.put("name",this.name);
		cardJson.put("type",this.type);
		cardJson.put("isWinner",this.isWinner);
		
		return cardJson;
	}
	
	/**
	 * Returns true of the provided object is equivalent to this card
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Card))
			return false;
		Card cardO = (Card) o;
		return Objects.equals(this.cardId, cardO.cardId) 
				&& Objects.equals(this.name, cardO.name) 
				&& Objects.equals(this.type, cardO.type);
	}

}
