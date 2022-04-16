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
import service.domain.jpa.Room2;
import service.domain.redis.RedisGaming;
import service.web.Gaming;
import service.webservice.GameService;
import service.webservice.UserService;

@Component
public class RedisChatHandler extends TextWebSocketHandler  {

	private static List<WebSocketSession> sessionList = new ArrayList<>();
	
	private static ArrayList<Room2> roomList = new ArrayList<Room2>();
	
	
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */
    private static int nextRoomId = 0;
    
    //세션이 들어간 방 정보
    private static HashMap<WebSocketSession, Integer> userRoomInfo = new HashMap<WebSocketSession, Integer>();
	
    public static UserService userService ;
    public static GameService gameService ;
    
	/*client가 서버에게 메시지 보냄*/
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        

        if (session != null) {
            String sessionId = session.getId();

            Integer romid = userRoomInfo.get(session);
            
            if(payload.equals("start")) {
            	roomList.get(romid).userclickStart(session);
            	
            	 //#룸에 다른 사람이 있을시 상대방에게 start 버튼을 클릭했다는 알림을 줌
            	if(roomList.get(romid).anotherUserCheck(session))
            		sendMessage(roomList.get(romid).anotherUser(session), makeJson(sendUserInfo(roomList.get(romid).anotherUser(session), "다른 유저가 start 클릭함")));
            	
            		//모든 유저가 시작을 눌렀을시 게임 시작
	            if(roomList.get(romid).userAllReStart()) {
	            	roomList.get(romid).setGaming(new RedisGaming());
	            	gameService.gameStart(roomList.get(romid).getGaming());
	            
	            	roomList.get(romid).setGamingCheck(false);
	            	
	            	sendMessage(session, makeJson(sendUserInfo(session, "")));
	               	if(roomList.get(romid).anotherUserCheck(session))
	            		sendMessage(roomList.get(romid).anotherUser(session), makeJson(sendUserInfo(roomList.get(romid).anotherUser(session), "")));	            	
	            	
	            }
	            		
	            		
            }
            else if(payload.equals("left") && roomList.get(romid).getGaming().isMakedBlock() ) {
            	
	            if(roomList.get(romid).getGaming().isStartCheck()) {
	            	gameService.leftMove(roomList.get(romid).getGaming());
	            	
	            	sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		WebSocketSession anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "left")));
	            	}
	            	
	            }
            }
            else if(payload.equals("right") && roomList.get(romid).getGaming().isMakedBlock() ) {
            	if(roomList.get(romid).getGaming().isStartCheck()) {
            		gameService.rightMove(roomList.get(romid).getGaming());
            	
            		sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		WebSocketSession anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "right")));
	            	}
            		
            	}
            }
            else if(payload.equals("convert") && roomList.get(romid).getGaming().isMakedBlock() ) {
            	if(roomList.get(romid).getGaming().isStartCheck())  {   
            		gameService.convertBlock(roomList.get(romid).getGaming());
            		
            		sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		WebSocketSession anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "convert")));
	            	}
            		
            	}
            }
            else {

            }     
            
        }

    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	
        if (session != null) {
        	// db에 유저 정보 저장
        	userService.addUser(session);
        	userService.addConnLog(session);
        	
        	
        	//새로운 방 만들어야할지 정함
        	boolean makeRoomCheck = true ;
            sessionList.add(session);
            //빈방 있는지 검색후 방이 있을시 그 방에 들어감

            for(Room2 r : roomList) {
            	if(r.userAllJoin()) ;
            	else {
            		makeRoomCheck = false ;
            		r.userJoin(session);
            		userRoomInfo.put(session, r.getRoomId());
            		
            		sendMessage(session, makeJson(sendUserInfo(session, "접속성공")));
            		
            		//상대방에게도 메시지 보냄
            		sendMessage(r.anotherUser(session), makeJson(sendUserInfo(r.anotherUser(session), "다른 유저 들어옴")));

            	}
            	
            }
            if(makeRoomCheck) {
            //방이 없을시 새로운 방을 만듬
            roomList.add(new Room2());
            int roomId = nextRoomId;
            nextRoomId++;
            //방에 아이디 부여
            roomList.get(roomId).setRoomId(roomId);
            //방에 들어감
            roomList.get(roomId).userJoin(session);
            //user가 들어간 방 정보 등록
            userRoomInfo.put(session, roomId);
            sendMessage(session, makeJson(sendUserInfo(session, "접속성공")));
            }
              
            
        }

       
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session != null) {
       	 int roomId = userRoomInfo.get(session);
           //#룸에 다른 사람이 있을시 상대방에게 유저가 나갔다는 알림을 줌
           
           if(roomList.get(roomId).anotherUserCheck(session)) {
           	sendMessage(roomList.get(roomId).anotherUser(session), makeJson(sendUserInfo(roomList.get(roomId).anotherUser(session), "다른 유저 나감")));
           	
           }
           
           //세션리스트에서 제거
           sessionList.remove(session);
           //방에서 제거
           roomList.get(roomId).userOut(session);

       }
    }
    
    
    /**
     * 웹소켓 연결된 모든 클라이언트에 1초마다 
		gaming 진행상태 및 블록 렌딩 정보 전달
     */
	@Scheduled(cron = "0/1 * * * * ?")
	private void gameCheck() throws JsonProcessingException{
		
		for(int k=0; k<sessionList.size(); k++) {
			Integer romid = userRoomInfo.get(sessionList.get(k));
			

			try {
				
				//게임을 시작한 객체일시 gaming 연산(블럭 다운, 줄 체크 등) 시작
			if(roomList.get(romid).getGaming().isStartCheck()) {
				
				//게임 진행중인 방에서 중간에 한명 나갔을경우 게임 end전까지 진행은 되게해야함
				//#방에 인원이 한명인지 두명인지 알아본후 정해야함
				
				//중간에 한명이 나가서 방에서 한명이 게임줄일때
				if(!roomList.get(romid).anotherUserCheck(sessionList.get(k))) {
					
					gameService.gaming(roomList.get(romid).getGaming());
					
					HashMap sendInfo = comParaMeter(sessionList.get(k), "gaming", "");
					sendMessage(sessionList.get(k), makeJson(sendInfo));
					
				}
				//방에 2명이서 플레이 중일경우
				else {

					//room에서 첫번째로 게이밍 체크하는 경우
					if(!roomList.get(romid).isGamingCheck()) {
						//게이밍 연산 실행 및 게임 진행 상태 확인
						
						boolean gamestatus = gameService.gaming(roomList.get(romid).getGaming());
						//전달할 게이밍 정보 생성
						HashMap sendInfo = comParaMeter(sessionList.get(k), "gaming", "");
						sendMessage(sessionList.get(k), makeJson(sendInfo));
						
						
						roomList.get(romid).setGamingCheck(true);
						
					}
					//room에서 두번째로 게이밍 체그하는 경우 ( 블럭 낙하를 2번 시행되지 않도록 게이밍 객체 연산은 진행하지 않음
					else {
						//전달할 게이밍 정보 생성
						HashMap sendInfo = comParaMeter(sessionList.get(k), "gaming", "");
						sendMessage(sessionList.get(k), makeJson(sendInfo));
						roomList.get(romid).setGamingCheck(false);
					}
			}
	
		} else {
			//게임 시작 안한 객체는 패스
		  }
			} //중간에 로그아웃으로 nullPoint exception이 발생할 수 있음
			catch (NullPointerException e) {
				
			}
			
		}
				
	}
	
	//유저 들어오거나 나갈때 메시지 보냄
	public HashMap sendUserInfo(WebSocketSession session, String mesg) {
		
		HashMap userHash = new HashMap();
		if (session != null) {
		Integer romid = userRoomInfo.get(session);
		
		userHash.put("numOfUser", sessionList.size() );
		userHash.put("userId", session.getId() );
		userHash.put("roomId", romid);
		
		if(roomList.get(romid).anotherUserCheck(session)) {
			userHash.put("anoUserId", roomList.get(romid).anotherUser(session).getId());
		}
		else 
			userHash.put("anoUserId", "");		
			userHash.put("status", "userStatus" );
			userHash.put("mesg", mesg );
		//다른 유저 들어오고 나가나는 status 메시지 추가
		}
		return userHash ;
		
		
	}

	
	//게임 진행 공통 파라미터 모음
	public HashMap comParaMeter(WebSocketSession session) {
		Integer romid = userRoomInfo.get(session);
		
		HashMap curHash = roomList.get(romid).getGaming().getHash();
		curHash.put("block", gameService.getBlockLoc(roomList.get(romid).getGaming()) );
		curHash.put("score", roomList.get(romid).getGaming().getScore() );
		curHash.put("numOfUser", sessionList.size() );
		curHash.put("nextBlock", roomList.get(romid).getGaming().getNextBlock() );
		
		//status 및 상태메시지 추가
		//게임 진행중일때
		if(roomList.get(romid).isProgress()) {
			curHash.put("status", "gaming" );
			curHash.put("mesg", "" );
		} // 게임 end일때
		else {
			curHash.put("status", "end" );
			curHash.put("mesg", "END" );
		}
		return curHash;
	}

	//게임진행 공통 파라미터 모음 (다른 한명에게 메시지랑 함께 보낼때 사용)
	public HashMap comParaMeter(WebSocketSession session, String status, String mesg) {
		
		if (session != null) {
		Integer romid = userRoomInfo.get(session);
		
		HashMap curHash = roomList.get(romid).getGaming().getHash();
		curHash.put("block", gameService.getBlockLoc(roomList.get(romid).getGaming()) );
		curHash.put("score", roomList.get(romid).getGaming().getScore() );
		curHash.put("numOfUser", sessionList.size() );
		curHash.put("nextBlock", gameService.getNextBlock(roomList.get(romid).getGaming()) );
		
		//status 및 상태메시지 추가
		curHash.put("status", "gaming" );
		curHash.put("mesg", mesg );
		return curHash;
		
		}
		//메시지 보내기 직전 close()되어 session이 없을 경우 null 보냄
		else {
			return new HashMap();
		}
	}
	
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
