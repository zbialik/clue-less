package clueless;

import org.json.JSONObject;

/**
 * TODO: describe the class
 * @author zachbialik
 *
 */
public class RoomCard extends Card implements ClueInterface {
	
	public int roomId;

	RoomCard(int rId, int id, String n, boolean w) {
		super(id, n, w, CARD_TYPE_ROOM);
		this.roomId = rId;
	}
	
	RoomCard(int rId, int id, String n) {
		super(id, n, CARD_TYPE_ROOM);
		this.roomId = rId;
	}
	
	/**
	 * Returns information relevant to the room card as json
	 */
	public JSONObject toJson() {
		
		JSONObject roomCardJson = super.toJson();
		roomCardJson.put("roomId",this.roomId);
		
		return roomCardJson;
	}

}
