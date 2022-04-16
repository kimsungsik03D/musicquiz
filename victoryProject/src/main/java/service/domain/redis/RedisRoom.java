package service.domain.redis;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.socket.WebSocketSession;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash(value="room", timeToLive =1800)
@Getter
@Setter
@NoArgsConstructor
public class RedisRoom implements Serializable{
    
    private static final long serialVersionUID = 2327692830319429806L;
	//방에 들어온 유저 수다. 
	//@Setter(AccessLevel.NONE)
	//WebSocketSession[] userList = new WebSocketSession[2];
	
	@Id
	//방 아이디 정보
	private long roomId ;
	
	//유저 0과 1이 모두 ready를 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	boolean[] userReady = {false, false } ;
	
	//유저 0과 1이 모두 재시작을 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	boolean[] userRestart = {false, false } ;
	
	
	
	//게이밍 객체 정보
	private RedisGaming gaming ;
	
	
	//웹 객체가 게이밍 객체 연산을 처리했나 확인 (첫번째 유저를 통해 게이밍 연산을 진행했다면 true로 변경됨, 다른 유저 차례에서는 gaming 연산 안하고 패스후 false로 바뀜)
	private boolean gamingCheck= true;
	
	//방 객체가 게임중인지 확인
	private boolean progress ;
	
	@Builder()
	private RedisRoom(long roomId, RedisGaming gaming) {
		this.roomId = roomId ;
		this.gaming = gaming;

	}

}
