package service.domain;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import service.web.Gaming;

/*
 *멀티모드에서 사용되는 방 객체이다.
 * 협력모드에서는 방장이 없고 방에 인원 2명이 고정되어있기에 배열을 사용해 구현했다.
 * 
 * 
 * */

@Getter
@Setter
public class Room {

	//방에 들어온 유저 수다. 
	@Setter(AccessLevel.NONE)
	Session[] userList = new Session[2];
	
	//유저 0과 1이 모두 ready를 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	boolean[] userReady = {false, false } ;
	
	//유저 0과 1이 모두 재시작을 눌렀는지 알려준다.
	@Setter(AccessLevel.NONE)
	boolean[] userRestart = {false, false } ;
	
	//방 아이디 정보
	private int roomId ;
	
	//게이밍 객체 정보
	private Gaming gaming ;
	
	
	//웹 객체가 게이밍 객체 연산을 처리했나 확인 (첫번째 유저를 통해 게이밍 연산을 진행했다면 true로 변경됨, 다른 유저 차례에서는 gaming 연산 안하고 패스후 false로 바뀜)
	private boolean gamingCheck= false;
	
	//방 객체가 게임중인지 확인
	private boolean progress ;
	

	//start 클릭할때
	public void userclickStart(Session session) {
		for(int i=0; i<2; i++) {
			if(userList[i] == session) 
				userRestart[i] = true;
		}
	}
	
	
	//방 인원이 전부 들어왔는지 확인
	public boolean userAllJoin() {
		for(int i =0; i<2; i++) {
			
			if(userList[i] == null) 
				return false;	
		}	
		return true;
	}
	
	
	//user 들어올때
	public boolean userJoin(Session user) {
		
		 
		if(!userAllJoin()) {
		
		for(int i =0; i<2; i++) {
			if(userList[i] == null) {
				userList[i] = user ;

				break;
			}
			
		}	
		
		return true;
		}
		else {
		return false;
		}
	}	

	//user 나갈때
	public void userOut(Session user) {
		for(int i =0; i<2; i++) {
			if(userList[i] == user) {
				userList[i] = null ;
				break;
			}
		}	
	}
	
	//방의 다른 유저 정보 있는지 확인
	public boolean anotherUserCheck(Session user ) {
		for(int i=0; i<2; i++) {
			if(userList[i] != user && userList[i] != null) 
				return true ;
		}
		
		return false ;
	}	
	
	//방의 다른 유저 정보 전달
	public Session anotherUser(Session user ) {
		for(int i=0; i<2; i++) {
			if(userList[i] != null) 
				if(!userList[i].equals(user))
				return userList[i];
		}
		
		return null ;
	}
	
	/*user가 들어올때 (순서 지정) 리스트에서 사용될듯함
	public void userJoin(int order, Session user) {
		
		userList[order] = user ;
	}
	*/
	
	/*user가 나갈때
	public void userOut(int order) {
		
		userList[order] = null ;
	}
	*/
	
	//모든 유저가 ready를 눌렀나 확인
	public boolean userAllReady() {
		for(int i=0; i<2; i++) {
			if(!userReady[i])
				return false;
		}
		
		return true;
	}
	
	
	//모든 유저가 재시작을 눌렀나 확인후 전부 재시작 눌렀을시 false로 변환
	public boolean userAllReStart() {
		for(int i=0; i<2; i++) {
			if(!userRestart[i])
				return false;
		}
		
		//전부 재시작 눌렀을경우 false로 초기화하고 true 반환
		for(int i=0; i<2; i++) {
			userRestart[i] = false;
		}
		
		return true;
	}	
	
	
}
