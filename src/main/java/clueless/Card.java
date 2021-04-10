package clueless;

import java.util.Objects;

import org.json.JSONObject;

/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class Card {
	
	public String name;
	public String type;
	public boolean isWinner;
	
	Card(String n, boolean w, String tp) { // constructor
		this.name = n;
		this.isWinner = w;
		this.type = tp;
	}
	
	Card(String n, String tp) { // if boolean not provided, assume false
		this.name = n;
		this.isWinner = false;
		this.type = tp;
	}
	
	/**
	 * Returns information relevant to the card as json
	 */
	public JSONObject toJson() {
		
		JSONObject cardJson = new JSONObject();
		cardJson.put("name",this.name);
		cardJson.put("type",this.type);
		cardJson.put("isWinner",this.isWinner);
		
		return cardJson;
	}
	
	/**
	 * Returns true if the card is the provided type
	 * @param cardType
	 * @return isType
	 */
	public boolean isType(String cardType) {
		
		if (this.type.equals(cardType)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns true of the provided object is equivalent to this card
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Card)) {
			return false;
		} else {
			Card cardO = (Card) o;
			return Objects.equals(this.name, cardO.name) 
					&& Objects.equals(this.type, cardO.type);
		}
	}

}
