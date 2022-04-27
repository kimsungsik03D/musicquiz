package service.webservice;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.socket.WebSocketSession;

import service.web.Gaming;

public interface GameService {

	 Gaming gameStart(String sessionId,JSONObject gameSet);
	 boolean gameCtrl(String answer, Gaming redisGame);
	 boolean timeHintCheck(Gaming redisGame);
	 boolean answerCheck(String answer, Gaming redisGame);
	 void rankSave(Gaming redisGame, WebSocketSession session);
	 
	 ResponseEntity<JSONObject> getRankList();
	
	//hihi
}
