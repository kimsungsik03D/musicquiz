package service.web.handler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.domain.jpa.Room;
import service.web.Gaming;
import service.webservice.GameService;
import service.webservice.UserService;

@Component
public class SoloHandler extends TextWebSocketHandler  {

	private static List<WebSocketSession> sessionList = new ArrayList<>();
	
	
	
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */

	
    public static UserService userService ;
    public static GameService gameService;
    
	/*client가 서버에게 메시지 보냄*/
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        

        if (session != null) {
  
        }
     }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {


 
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }
    
    
    /**
     * 웹소켓 연결된 모든 클라이언트에 1초마다 
		gaming 진행상태 및 블록 렌딩 정보 전달
     */
	@Scheduled(cron = "0/1 * * * * ?")
	private void gameCheck() throws JsonProcessingException{
		
				
	}
	
	//유저 들어오거나 나갈때 메시지 보냄
	public HashMap sendUserInfo(WebSocketSession session, String mesg) {
		
		HashMap userHash = new HashMap();
		if (session != null) {

		
		userHash.put("numOfUser", sessionList.size() );
		userHash.put("userId", session.getId() );
		
		}
		return userHash ;
		
		
	}

	/*
	//게임 진행 공통 파라미터 모음
	public HashMap comParaMeter(WebSocketSession session) {

	}

	//게임진행 공통 파라미터 모음 (다른 한명에게 메시지랑 함께 보낼때 사용)
	public HashMap comParaMeter(WebSocketSession session, String status, String mesg) {
		

	}*/
	
	//현재 게임 상태 객체로 만들어서 반환
	private TextMessage makeJson(HashMap data) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(data);
			return new TextMessage(json) ;
		} catch (JsonProcessingException e) {
			System.out.println("json 에러");
			return null;
		}
	}
	
	private void sendMessage(WebSocketSession session, TextMessage mesg) {
		if (session != null) {
			if(session.isOpen() && mesg !=null)
				try {
					session.sendMessage(mesg);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
	}
}
