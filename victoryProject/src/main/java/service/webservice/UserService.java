package service.webservice;

import org.springframework.web.socket.WebSocketSession;

public interface UserService {

	void addUser(WebSocketSession session);
	
	void addConnLog(WebSocketSession session);
}
