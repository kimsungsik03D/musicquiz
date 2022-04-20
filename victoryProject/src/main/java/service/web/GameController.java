package service.web;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class GameController {

	
	Gaming gameing ;
	
	private boolean start = false;
	
	@GetMapping("/")
	public String startIndex() {
		return "view";
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
