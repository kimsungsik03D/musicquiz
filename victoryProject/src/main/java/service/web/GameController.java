package service.web;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import service.domain.block.Node;

@Controller
public class GameController {

	
	Gaming gameing ;
	
	private boolean start = false;
	
	@GetMapping("/")
	public String startIndex() {
		return "view";
	}
	
	@GetMapping("/start") 
	public String gameStart(){
		gameing.gameStart();
		start = true;
		
		return "success";
		
	}
	
	@RequestMapping(value = "/end")
	public String gameEnd(){
		return "end";
	}
	
	@GetMapping("/leftMove") 
	public String leftMove(){
		gameing.leftMove();
		
		return "";
		
	}
	
	@GetMapping("/rightMove") 
	public String rightMove(){
		gameing.rightMove();
		
		return "";
		
	}	
	
	
	//스케줄러로 1초마다 gaming 및 블록 렌딩을 호출하여 게임상태 점검
	@Scheduled(cron = "0/1 * * * * ?")
	private String gameCheck(){
		
		if(start) {
		if(!gameing.gaming()) {
			for(int i=0; i<10; i++) {
				for(int j=0; j<6; j++) {
					if(gameing.getHash().get(i*6 +j)) {
						System.out.print("*");
					}
					else {
						System.out.print(" ");
					}
				}
				System.out.println();
			}
			System.out.println("게임끝");
			
			return "redirect:/end";
		}
		else {
			gameing.convertBlock();
			//gameing.leftMove();
			
			for(Node i: gameing.getBlockInfo().getBlock()) {
				
				
				System.out.println("블록위치" + (i.getX()*gameing.getColumns() + i.getY()));
			}
			
			for(int i=0; i<10; i++) {
				for(int j=0; j<6; j++) {
					if(gameing.getHash().get(i*6 +j)) {
						System.out.print("*");
					}
					else {
						System.out.print(" ");
					}
				}
				System.out.println();
			}
			
			//rest컨트롤러 별개로 만들것
			//스코어 점수 파라미터에 추가
			
			//블록판 상태 갱신 
		}

		return "" ;
		
		}
		else {
			return "" ;
		}
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
