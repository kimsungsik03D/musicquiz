/*
 * 멀티게임 버전 게임 서비스 구현
 * */

package service.webservice;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import service.domain.jpa.Room;
import service.domain.jpa.Song;
import service.domain.jpa.SongRepository;
import service.domain.jpa.log.RoomRepository;
import service.web.MultiGaming;
import service.web.dto.RoomDto;

@Service
public class MultiGameServiceImpl implements MultiGameService{
	@Autowired
	public SongRepository songRepo ;
	
	@Autowired
	public RoomRepository roomRepo;
	
	Random random = new Random();
	
	//게임 한판당 시간
	private final int ROUNDTIME = 30;
	
	
	//!dto 추가 작업 해야함
	public MultiGaming gameStart(String roomId, JSONObject gameSet) {
	
		//게임 초기 설정해야하는 목록
		int toYear = 1990;
		int fromYear = 2021;
		MultiGaming redisGame = MultiGaming.builder()
				.questionCount(Integer.parseInt(String.valueOf(gameSet.get("questionCount"))))
				.roomId(roomId)
				.singerHintCheck((boolean) gameSet.get("singerHint"))
				.songHintCheck((boolean) gameSet.get("songHint"))
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
	public boolean gameCtrl(String userId, String answer, MultiGaming redisGame) {
		
		//시간 오버인지 체크
		if(timeOverCheck(redisGame)) {
			
			//시간 오버일경우 30초 지정
			redisGame.setClearTime(redisGame.getClearTime()+ ROUNDTIME*1000);
			
			redisGame.setQuestionCount(redisGame.getQuestionCount()-1);
			//시간오버일때 다음라운드 진행 여부 체크
			if(gameEndCheck(redisGame)) {
				
				return false;
			}
			sendUri(redisGame);
			redisGame.setRemainTime(ROUNDTIME);
			redisGame.setStarRoundTime(LocalDateTime.now());
			
		}
		else {
		
		//정답이 맞았을때
		if(answerCheck(answer, redisGame)) {
			//System.out.println("정답 맞음");
			//round 클리어 타임 저장
			saveClearTime(redisGame);
			redisGame.getScore().replace(userId, (redisGame.getScore().get(userId)+1));
			redisGame.setQuestionCount(redisGame.getQuestionCount()-1);
			//System.out.println("남은 문제수:"+ redisGame.getQuestionCount());
			
			//게임 엔드인지 혹은 다음라운드 진행해야하는지 체크
			if(gameEndCheck(redisGame)) {
				
				return false;
			}else {
				sendUri(redisGame);
				redisGame.setRemainTime(ROUNDTIME);
				redisGame.setStarRoundTime(LocalDateTime.now());
				
			}

			
		}
		//정답 틀리고 힌트를 줘야하는 시간일시
		else {
			//설계 변경으로 게임 진행 절반이상 지났을시 힌트 제공은 핸들러에서 obj에 추가하는것으로 변경함
			
		
		}
			
		} //# 시간 오버인지 체크 if문
		
		
		return true;
	}
	
	//게임 객체에 플레이 갯수에 맞추어 조건에 맞는 노래 id 리스트 저장하는함수
	private void songRandom(MultiGaming redisGame) {

		List<Integer> songList = songRepo.getSongId(redisGame.getToYear(), redisGame.getFromYear());
		
		Collections.shuffle(songList);
		if(redisGame.getQuestionCount() >songList.size() ) {
		//questionCount 보다 적은지 확인해야함
			redisGame.setSongList(songList);
			//System.out.println(songList.size());
			
		}
		else
			redisGame.setSongList(songList.subList(0, redisGame.getQuestionCount()));
		
	}
	
	
	public int songCountCheck(MultiGaming redisGame) {
		
		List<Integer> songList = songRepo.getSongId(redisGame.getToYear(), redisGame.getFromYear());
		
		if(redisGame.getQuestionCount()>songList.size() )
			return songList.size();
		else
			return -1;
		
	}
	
	//노래 uri 제공할때 랜덤으로 id에맞는 편집본 하나를 뽑아서 제공
	private void sendUri(MultiGaming redisGame) {
		
		int songNum = random.nextInt(3)+1 ;
		Song song = songRepo.getSongInfo(redisGame.getSongList().get(0), songNum).get();
		redisGame.getSongList().remove(0);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://docs.google.com/uc?export=open&id=").append(song.getUrlId());
		
		//url 받아올때 힌트랑 정답 함께 저장함
		redisGame.setAnswerName(song.getTitle());
		redisGame.setUri(stringBuilder.toString());
		redisGame.setSongHint(song.getTitleHint());
		redisGame.setSingerHint(song.getSinger());
	
		
	}
	
	//게임 끝내야 되는지 검증 true면 게임 End, flase면 아직 진행중
	private boolean gameEndCheck(MultiGaming redisGame) {
		if(redisGame.getQuestionCount() < 1) 
			return true ;
		else
			return false ;
		
	}
	
	// 게임 남은 시간초 비교 true면 시간초 over되어 round 줄임, false면 아직 라운드 진행중
	private boolean timeOverCheck(MultiGaming redisGame) {
		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);

		
		redisGame.setRemainTime(ROUNDTIME - duration.getSeconds());
		
		if(duration.getSeconds() > 30) {
			return true;
		}
		else {
			return false ;
		}
	
	}
	// 게임 남은 시간초 비교 true면 Hint제공, false면 Hint 제공 안됨
	public boolean timeHintCheck(MultiGaming redisGame) {
		

		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);
		
		
		
		if( duration.getSeconds() > ROUNDTIME/2 ) 
			return true ;
		else
			return false ;
	
	}
	// 정답 맞았나 체크 맞으면 true, 틀리면 false
	public boolean answerCheck(String answer, MultiGaming redisGame) {
		
	
		if(answer.replace(" ", "").toLowerCase().equals(redisGame.getAnswerName().replace(" ", "").toLowerCase()))
			return true;
		else 
			return false;
		
		
	}
	//Round 끝날때마다 클리어 타임이 얼마나 걸렸나 확인
	private void saveClearTime(MultiGaming redisGame) {
		
		LocalDateTime startTime =redisGame.getStarRoundTime();
		
		LocalDateTime currentTime = LocalDateTime.now();
		
		Duration duration = Duration.between(startTime, currentTime);
		
		
		//나노초를 밀리초로 변환
		int saveTime = duration.getNano()/1000000 ;
		int saveSecondTime = Long.valueOf(duration.getSeconds()).intValue() * 1000 ;
		
		redisGame.setClearTime(redisGame.getClearTime() + saveTime + saveSecondTime);
		//System.out.println("savedTime"+redisGame.getClearTime());
		
	}
	
	//현재 생성된 방 목록들의 상태 보여줌
	public ResponseEntity<JSONObject> getRoomList() {
    	JSONObject resultObj = new JSONObject();  
    	
    	Iterator<Room> itr =  roomRepo.findAll().iterator();
    	List<RoomDto> roomList = new ArrayList<RoomDto>();
    	
    	
    	while(itr.hasNext()) {
    		Room room = itr.next() ;

    		if(room !=null && room.getUserList().size() > 0)
    			roomList.add(RoomDto.builder()
    				.roomId(room.getRoomId())
    				.startCheck(room.isProgress())
    				.roomTitle(room.getRoomTitle())
    				.userList(room.getUserList())
    				.build()) ;
    		
    	}

    	
    	try {
    		resultObj.put("result","true");
    		resultObj.put("roomList", roomList);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
    	}
    	catch (Exception e) {
    		resultObj.put("result","false");
    		resultObj.put("reason",e);
    		return new ResponseEntity<JSONObject>(resultObj, HttpStatus.ACCEPTED);
    	}
		
	}
}
