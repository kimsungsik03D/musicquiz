package service.webservice;

import service.domain.jpa.Room;

public interface RoomService {
	public boolean userclickStart(Room room, String session);
	public boolean userJoin(Room room, String session);
	public boolean userOut(Room room, String session);
	public Room roomCreate(String roomId);
	public boolean ownerSet(Room room, String sessionId);
	public boolean titleSet(Room room, String title);
}
