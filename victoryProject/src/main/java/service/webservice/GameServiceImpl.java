package service.webservice;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.domain.jpa.Song;
import service.domain.jpa.SongRepository;
import service.web.Gaming;

@Service
public class GameServiceImpl implements GameService {
	
	@Autowired
	public SongRepository songRepo ;
	
	Random random = new Random();
	
	private final int roundTime = 30;
	
	//!dto 추가 작업 해야함
	public Gaming gameStart(String sessionId, JSONObject gameSet) {

		
		//게임 초기 설정해야하는 목록
		int toYear = Integer.parseInt(String.valueOf(gameSet.get("toYear")));
		int fromYear = Integer.parseInt(String.valueOf(gameSet.get("fromYear")));
		
		
		Gaming redisGame = Gaming.builder()
		.questionCount(Integer.parseInt(String.valueOf(gameSet.get("questionCount"))))
		.sessionId(sessionId)
		.singerHintCheck((boolean) gameSet.get("singerHint"))
		.songHintCheck((boolean) gameSet.get("songHint"))
		.rankMod((boolean) gameSet.get("rankMod"))
		.toYear(toYear)
		.fromYear(fromYear)
		.build();
		redisGame.setStarRoundTime(LocalDateTime.now());
		//노래 리스트 저장 함수
		songRandom(redisGame);
		sendUri(redisGame);
		
		return redisGame;
	}
	
	//답안 받았을때의 처리
	//한판이 30초라고 가정, 힌트는 절반이상 흘렀을때 제공
	//!리턴 타입 json으로 변경
	//게임 end일시 false , 진행해야하면 true
	public boolean gameCtrl(String answer, Gaming redisGame) {
		
		//시간 오버인지 체크
		if(timeOverCheck(redisGame)) {
			
			//시간 오버일경우 30초 지정
			redisGame.setClearTime(redisGame.getClearTime()+ roundTime*1000);
			
			redisGame.setQuestionCount(redisGame.getQuestionCount()-1);
			//시간오버일때 다음라운드 진행 여부 체크
			if(gameEndCheck(redisGame)) {
				
				return false;
			}
			sendUri(redisGame);
			redisGame.setRemainTime(roundTime);
			redisGame.setStarRoundTime(LocalDateTime.now());
			
		}
		else {
		
		//정답이 맞았을때
		if(answerCheck(answer, redisGame)) {
			System.out.println("정답 맞음");
			//round 클리어 타임 저장
			saveClearTime(redisGame);
			redisGame.setScore(redisGame.getScore()+1);
			redisGame.setQuestionCount(redisGame.getQuestionCount()-1);
			System.out.println("남은 문제수:"+ redisGame.getQuestionCount());
			
			//게임 엔드인지 혹은 다음라운드 진행해야하는지 체크
			if(gameEndCheck(redisGame)) {
				
				return false;
			}else {
				sendUri(redisGame);
				redisGame.setRemainTime(roundTime);
				redisGame.setStarRoundTime(LocalDateTime.now());
				
			}

			
		}
		//정답 틀리고 힌트를 줘야하는 시간일시
		else {
			System.out.println("정답 틀림");
			System.out.println(redisGame.getAnswerName());
			//설계 변경으로 게임 진행 절반이상 지났을시 힌트 제공은 핸들러에서 obj에 추가하는것으로 변경함
			
			
		}
			
			
		
		
		} //# 시간 오버인지 체크 if문
		
		
		
		return true;
	}
	
	//게임 객체에 플레이 갯수에 맞추어 조건에 맞는 노래 id 리스트 저장하는함수
	private void songRandom(Gaming redisGame) {

		List<Integer> songList = songRepo.getSongId(redisGame.getToYear(), redisGame.getFromYear());
		Collections.shuffle(songList);
		
		//questionCount 보다 적은지 확인해야함
		redisGame.setSongList(songList.subList(0, redisGame.getQuestionCount()));
		
		
		
	}
	//노래 uri 제공할때 랜덤으로 id에맞는 편집본 하나를 뽑아서 제공
	private void sendUri(Gaming redisGame) {
		
		int songNum = random.nextInt(3)+1 ;
		Song song = songRepo.getSongInfo(redisGame.getSongList().get(0), songNum).get();
		redisGame.getSongList().remove(0);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("'https://docs.google.com/uc?export=open&id=").append(song.getUrlId()).append("'");
		
		//url 받아올때 힌트랑 정답 함께 저장함
		redisGame.setAnswerName(song.getTitle());
		redisGame.setUri(stringBuilder.toString());
		redisGame.setSongHint(song.getTitleHint());
		redisGame.setSingerHint(song.getSinger());
	
		
	}
	
	//게임 끝내야 되는지 검증 true면 게임 End, flase면 아직 진행중
	private boolean gameEndCheck(Gaming redisGame) {
		if(redisGame.getQuestionCount() < 1) 
			return true ;
		else
			return false ;
		
	}
	
	// 게임 남은 시간초 비교 true면 시간초 over되어 round 줄임, false면 아직 라운드 진행중
	private boolean timeOverCheck(Gaming redisGame) {
		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);

		
		
		redisGame.setRemainTime(roundTime - duration.getSeconds());
		
		if(duration.getSeconds() > 30) {
			return true;
		}
		else {
			return false ;
		}
	
	}
	// 게임 남은 시간초 비교 true면 Hint제공, false면 Hint 제공 안됨
	public boolean timeHintCheck(Gaming redisGame) {
		

		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);
		
		
		
		if( duration.getSeconds() > roundTime/2 ) 
			return true ;
		else
			return false ;
	
	}
	// 정답 맞았나 체크 맞으면 true, 틀리면 false
	public boolean answerCheck(String answer, Gaming redisGame) {
		
		if(answer.equals(redisGame.getAnswerName()))
			return true;
		else 
			return false;
		
		
	}
	//Round 끝날때마다 클리어 타임이 얼마나 걸렸나 확인
	private void saveClearTime(Gaming redisGame) {
		
		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);
		
		
		//나노초를 밀리초로 변환
		int saveTime = duration.getNano()/1000000 ;
		int saveSecondTime = Long.valueOf(duration.getSeconds()).intValue() * 1000 ;
		
		redisGame.setClearTime(redisGame.getClearTime() + saveTime + saveSecondTime);
		System.out.println("savedTime"+redisGame.getClearTime());
		
	}
	
	//노래 리스트의 정답 호출
}
