package service.webservice;

import org.springframework.stereotype.Service;

import service.domain.jpa.Room;
import service.web.MultiGaming;

@Service
public class RoomServiceImpl implements RoomService {
	
	
	//방생성
	public Room roomCreate(String roomId) {
		//방에 아이디 부여
		Room room = new Room();
		room.setRoomId(roomId);
		room.setGaming(new MultiGaming());
		return room;
	}
	
	
	//start나 ready 클릭할때
	//게임 시작 조건이 충족될시 true, 아닐시 false 
	public boolean userclickStart(Room room, String session) {
		if(room.getUserList().contains(session) && !room.getRoomOwner().equals(session) ) {
			if(room.getUserReady().contains(session)) 
				room.getUserReady().add(session);
			else
				room.getUserReady().remove(session);

			return false;
		}
		else if(room.getUserList().contains(session) && room.getRoomOwner().equals(session) ) {
			if(room.getUserReady().size()== room.getUserList().size()-1) {
			
				room.setProgress(true);
				return true;
			}	
			else
				return false;

		}
		
		return false;
	}
	
	
	//user 들어올때
	public boolean userJoin(Room room, String sessionId) {
		
		if(!room.getUserList().contains(sessionId) ) {
			room.getUserList().add(sessionId);
			return true;
		}	
		else 
			return false;
		
	}

	//user 나갈때
	public boolean userOut(Room room, String sessionId) {
		
		if(room.getUserList().contains(sessionId) ) {
			room.getUserList().remove(sessionId);
			return true;
		}	
		else 
			return false;
		
	}
	
	//방장 설정
	public boolean ownerSet(Room room, String sessionId) {

			room.setRoomOwner(sessionId);
			return true;
	}
	

		


	
}
