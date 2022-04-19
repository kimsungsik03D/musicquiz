package service.webservice;


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
	
	//!dto 추가 작업 해야함
	public Gaming gameStart(String sessionId, JSONObject gameSet) {

		
		//게임 초기 설정해야하는 목록
		int toYear = Integer.parseInt(String.valueOf(gameSet.get("toYear")));
		int fromYear = Integer.parseInt(String.valueOf(gameSet.get("fromYear")));
		
		
		Gaming redisGame = Gaming.builder()
		.questionCount(Integer.parseInt(String.valueOf(gameSet.get("questionCount"))))
		.sessionId(sessionId)
		.singerHint((boolean) gameSet.get("singerHint"))
		.songHint((boolean) gameSet.get("songHint"))
		.rankMod((boolean) gameSet.get("rankMod"))
		.toYear(toYear)
		.fromYear(fromYear)
		.build();
		
		//노래 리스트 저장 함수
		songRandom(redisGame);
		
		//랜덤 uri 테스트
		sendUri(redisGame);

		return redisGame;
	}
	
	//답안 받았을때의 처리
	//한판이 30초라고 가정, 힌트는 절반이상 흘렀을때 제공
	//!리턴 타입 json으로 변경
	public boolean gameCtrl(String answer, Gaming redisGame) {
		
		
		
		//정답이 맞았을때
		
		//힌트 줘야하는 시간이면 힌트 제공
		
		//엔드여부 확인후 엔드가 아닐시 다음 노래 주소 제공
		
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
		stringBuilder.append("'https://docs.google.com/uc?export=open&id=").append(song.getUrlId()).append("' type='audio/mp3'");
		
		redisGame.setAnswerName(song.getTitle());
		redisGame.setUri(stringBuilder.toString());
		
		System.out.println(redisGame.getUri());
		
	}
	
	//노래 리스트의 정답 호출
}
