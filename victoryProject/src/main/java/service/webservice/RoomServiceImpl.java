package service.webservice;

import org.springframework.web.socket.WebSocketSession;

public class RoomServiceImpl {

	/*
	//start 클릭할때
	public void userclickStart(WebSocketSession session) {
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
	public boolean userJoin(WebSocketSession user) {
		
		 
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
	public void userOut(WebSocketSession user) {
		for(int i =0; i<2; i++) {
			if(userList[i] == user) {
				userList[i] = null ;
				break;
			}
		}	
	}
	
	//방의 다른 유저 정보 있는지 확인
	public boolean anotherUserCheck(WebSocketSession user ) {
		for(int i=0; i<2; i++) {
			if(userList[i] != user && userList[i] != null) 
				return true ;
		}
		
		return false ;
	}	
	
	//방의 다른 유저 정보 전달
	public WebSocketSession anotherUser(WebSocketSession user ) {
		for(int i=0; i<2; i++) {
			if(userList[i] != null) 
				if(!userList[i].equals(user))
				return userList[i];
		}
		
		return null ;
	}
		
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
	
	*/
}
