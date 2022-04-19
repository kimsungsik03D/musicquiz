//실제 게이밍 구현
//소켓 관리 부분에서 게이밍 객체를 session 아이디와 함께 리스트에 저장하도록함

package service.web;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Gaming {
	

	private String sessionId ;
	
	//라운드 시작 시간
	LocalDateTime starRountTime ;
	//남은 문제 수
	int questionCount ;

	boolean songHint = false ;
	boolean singerHint = false ;
	
	//score가 기록되는 랭킹모드인지
	boolean rankMod = false ;

	int toYear = 0;
	int fromYear = 0;
	
	//게임 했는지 확인
	boolean startCheck = false ;
	
	String answerName = "";
	
	String uri = "";
	
	//점수
	private int score = 0;
	//노래 리스트 
	private List<Integer> songList ;
	//게임 시작 시간
	LocalDateTime startGameTime = LocalDateTime.now();	

	
    	
	@Builder()
	public Gaming(int questionCount, boolean songHint, boolean singerHint, boolean rankMod, String sessionId, int toYear, int fromYear ) {
		this.questionCount = questionCount ;
		this.songHint = songHint ;
		this.singerHint = singerHint;
		this.rankMod = rankMod ;
		this.sessionId = sessionId;
		this.toYear = toYear;
		this.fromYear = fromYear;
	}
	
	

	
}
