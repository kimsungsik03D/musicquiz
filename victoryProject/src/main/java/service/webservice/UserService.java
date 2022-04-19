package service.webservice;

import org.springframework.web.socket.WebSocketSession;

public interface UserService {

	
	void addConnLog(WebSocketSession session);
	
	void addDisLog(WebSocketSession session);
}
