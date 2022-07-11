package service.web;

import java.util.Timer;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import service.webservice.GameService;




@RequiredArgsConstructor
@Controller
@CrossOrigin(origins="*")
public class GameController {

		
	@GetMapping("/")
	public String startIndex() {
		return "view2";
	}
	
	
	//@Scheduled(cron = "0/5 * * * * ?")
	private String test(){
		Timer timer = new Timer();
		System.out.println("테스트");
		return "redirect:/end";
	}
	
	//블록 이동 이벤트
	
	//블록 전환 이벤트
	
}
