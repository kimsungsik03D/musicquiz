package service.domain.jpa;



import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import service.web.MultiGaming;

/*
 *멀티모드에서 사용되는 방 객체이다.
 * 협력모드에서는 방장이 없고 방에 인원 2명이 고정되어있기에 배열을 사용해 구현했다.
 * 
 * 
 * */
@RedisHash(value="game", timeToLive =18000)
@Getter
@Setter
public class Room {
	

	//방에 들어온 유저 수다. 
	@Setter(AccessLevel.NONE)
	List<String> userList = new ArrayList<String>();
	
	//유저들이 모두 ready를 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	List<String> userReady = new ArrayList<String>();
	
	//유저 0과 1이 모두 재시작을 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	List<String> userRestart = new ArrayList<String>();
	
	//방 아이디 정보
	@Id
	private String roomId ;
	
	//방장 아이디 정보
	private String roomOwner;
	
	//게이밍 객체 정보
	private MultiGaming gaming ;
	
	private String roomTitle;
	
	
	//웹 객체가 게이밍 객체 연산을 처리했나 확인 (첫번째 유저를 통해 게이밍 연산을 진행했다면 true로 변경됨, 다른 유저 차례에서는 gaming 연산 안하고 패스후 false로 바뀜)
	private boolean gamingCheck= false;
	
	//방 객체가 게임중인지 확인
	private boolean progress = false;
	

	//강퇴 리스트
	@Setter(AccessLevel.NONE)
	private List<String> blackList = new ArrayList<String>();
	
}
