package service.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MultiGaming  {
	private String roomId ;
	
	//라운드 시작 시간
	LocalDateTime starRoundTime ;
	//남은 문제 수
	int questionCount ;

	boolean songHintCheck = false ;
	boolean singerHinCheckt = false ;
	

	int toYear = 0;
	int fromYear = 0;
	
	///////////////설정 정보 End///////////////
	
	
	//게임 했는지 확인
	boolean startCheck = false ;
	
	String answerName = "";
	
	String uri = "";
	
	String songHint = "";
	String singerHint = "";
	

	//노래 리스트 
	private List<Integer> songList ;
	//게임 시작 시간
	LocalDateTime startGameTime = LocalDateTime.now();	

	//현재 라운드 남은 시간
	long remainTime = 0;
	
	//게임 클리어 시간
	int clearTime = 0;
	
	//각 유저마다 정답 맞춘수
	HashMap<String, Integer> score = new HashMap<String, Integer>();
	
    	
	@Builder()
	public MultiGaming(int questionCount, boolean songHintCheck, boolean singerHintCheck,  String roomId, int toYear, int fromYear, String username ) {
		this.questionCount = questionCount ;
		this.songHintCheck = songHintCheck ;
		this.singerHinCheckt = singerHintCheck;
		this.toYear = toYear;
		this.fromYear = fromYear;
		this.roomId = roomId;
	}
}
