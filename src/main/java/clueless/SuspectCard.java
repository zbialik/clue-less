package clueless;

import org.json.JSONObject;

public class SuspectCard extends Card implements ClueInterface {
	
	public int characterId;

	SuspectCard(int sId, int id, String n, boolean w) {
		super(id, n, w, CARD_TYPE_SUSPECT);
		this.characterId = sId;
	}
	
	SuspectCard(int sId, int id, String n) {
		super(id, n, CARD_TYPE_SUSPECT);
		this.characterId = sId;
	}
	
	/**
	 * Returns the characterId for this suspect card
	 * @return
	 */
	public int getCharacterId() {
		return this.characterId;
	}
	
	/**
	 * Returns information relevant to the suspect card as json
	 */
	public JSONObject toJson() {
		
		JSONObject suspectCardJson = super.toJson();
		suspectCardJson.put("characterId",this.characterId);
		
		return suspectCardJson;
	}
	
}
