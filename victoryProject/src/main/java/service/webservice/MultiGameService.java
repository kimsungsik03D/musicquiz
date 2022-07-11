package service.webservice;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.socket.WebSocketSession;


import service.web.MultiGaming;

public interface MultiGameService {
	MultiGaming gameStart(String roomId,JSONObject gameSet);
	 boolean gameCtrl(String userId, String answer, MultiGaming redisGame);
	 boolean timeHintCheck(MultiGaming redisGame);
	 boolean answerCheck(String answer, MultiGaming redisGame);
	 
	 int songCountCheck(MultiGaming redisGame);
}
