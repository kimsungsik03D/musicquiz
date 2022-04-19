package service.webservice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import service.domain.jpa.User;
import service.domain.jpa.UserRepository;
import service.domain.jpa.log.ConnLog;
import service.domain.jpa.log.ConnLogRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	public UserRepository userRepo ;
	
	@Autowired
	public ConnLogRepository connRepo;
	

	
	public void addConnLog(WebSocketSession session) {
		
		String ip = (String) session.getAttributes().get("ip") ;
		connRepo.save(ConnLog.builder()
				.session(session.toString())
				.ipAddress(ip)
				.build());
	}
	

	public void addDisLog(WebSocketSession session) {
		
		String ip = (String) session.getAttributes().get("ip") ;
		connRepo.save(ConnLog.builder()
				.session(session.toString())
				.ipAddress(ip)
				.build());
		
		
	}
	
}
