package service.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import service.domain.jpa.Room;
import service.domain.jpa.log.RoomRepository;
import service.web.MultiGaming;

@Service
public class RoomServiceImpl implements RoomService {
	

	@Autowired
	public RoomRepository roomRepo;
	
	//방생성
	public Room roomCreate(String roomId) {
		//방에 아이디 부여
		Room room = new Room();
		room.setRoomId(roomId);
		room.setGaming(new MultiGaming());
		room.setRoomTitle(roomId);
		roomRepo.save(room);
		
		return room;
	}
	
	
	//start나 ready 클릭할때
	//게임 시작 조건이 충족될시 true, 아닐시 false 
	public boolean userclickStart(Room room, String sessionId) {
		if(room.getUserList().contains(sessionId) && !room.getRoomOwner().equals(sessionId) ) {
			if(!room.getUserReady().contains(sessionId)) 
				room.getUserReady().add(sessionId);
			else
				room.getUserReady().remove(sessionId);

			return false;
		}
		else if(room.getRoomOwner().equals(sessionId) ) {
			if(room.getUserReady().size()== room.getUserList().size()-1) {
			
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
			roomRepo.save(room);
			
			return true;
		}	
		else 
			return false;
		
	}

	//user 나갈때
	public int userOut(Room room, String sessionId) {
		
		if(room.getUserList().contains(sessionId) ) {
			room.getUserList().remove(sessionId);
			room.getUserReady().remove(sessionId);
			roomRepo.save(room);			
			
			if(room.getRoomOwner().equals(sessionId)) {
				ownerSet(room, room.getUserList().get(0));
				return 0;	
			}

			return 1;
		}	
		else 
			return -1;
		
	}
	
	//방장 설정
	public boolean ownerSet(Room room, String sessionId) {

			room.setRoomOwner(sessionId);
			return true;
	}
	
	
	//방제목 설정
	public boolean titleSet(Room room, String title) {

		room.setRoomTitle(title);
		return true;
}
		


	
}
