package service.web;

import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.webservice.GameService;
import service.webservice.MultiGameService;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="*")
public class GameRestController {

	
	private final GameService gameService ;
	
	private final MultiGameService multiGameService;
	
	
	@GetMapping("/rankList")
	public ResponseEntity<JSONObject> rankList() {
		
		//gameService.getRankList();
		return gameService.getRankList();
	}
	
	
	@GetMapping("/roomList")
	public ResponseEntity<JSONObject> getRoomList() {
		
		return multiGameService.getRoomList();
	}
	
}
