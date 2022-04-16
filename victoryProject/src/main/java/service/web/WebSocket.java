package service.web;

import java.util.ArrayList;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.domain.Room;

@Component
@ServerEndpoint("/websocket")
public class WebSocket {

	public WebSocket() {};
    /**
     * 서버에 접속한 웹소캣 목록 저장
     */
    private static ArrayList<Session> sessionList = new ArrayList<Session>();
    
    private static ArrayList<Room> roomList = new ArrayList<Room>();
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */
    private static int nextRoomId = 0;
    
    //멀티모드 방 테스트
    //private static ArrayList<HashMap<Session, Gaming>> gamgingList = new ArrayList<HashMap<Session, Gaming>>();
    
    //세션이 들어간 방 정보
    private static HashMap<Session, Integer> userRoomInfo = new HashMap<Session, Integer>();
    

    //현재 진행중인 솔로모드용 게이밍 리스트
    //private static HashMap<Session, Gaming> soloMap = new HashMap<Session, Gaming>();
    
    /**
     * 웹소켓 사용자 연결 성립하는 경우 호출
     */
    @OnOpen
    public void handleOpen(Session session) {
        if (session != null) {
        	//새로운 방 만들어야할지 정함
        	boolean makeRoomCheck = true ;
            sessionList.add(session);
            //빈방 있는지 검색후 방이 있을시 그 방에 들어감

            for(Room r : roomList) {
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
            roomList.add(new Room());
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
	
	
	//상대방에게도 메시지 보냄

    /**
     * 웹소켓 메시지(From Client) 수신하는 경우 호출
     */
    @OnMessage
    public void handleMessage(String message, Session session) {

        if (session != null) {
            String sessionId = session.getId();

            Integer romid = userRoomInfo.get(session);
            
            if(message.equals("start")) {
            	roomList.get(romid).userclickStart(session);
            	
            	 //#룸에 다른 사람이 있을시 상대방에게 start 버튼을 클릭했다는 알림을 줌
            	if(roomList.get(romid).anotherUserCheck(session))
            		sendMessage(roomList.get(romid).anotherUser(session), makeJson(sendUserInfo(roomList.get(romid).anotherUser(session), "다른 유저가 start 클릭함")));
            	
            		//모든 유저가 시작을 눌렀을시 게임 시작
	            if(roomList.get(romid).userAllReStart()) {
	            	roomList.get(romid).setGaming(new Gaming());
	            	roomList.get(romid).getGaming().gameStart();
	            	roomList.get(romid).setGamingCheck(false);
	            	
	            	sendMessage(session, makeJson(sendUserInfo(session, "")));
	               	if(roomList.get(romid).anotherUserCheck(session))
	            		sendMessage(roomList.get(romid).anotherUser(session), makeJson(sendUserInfo(roomList.get(romid).anotherUser(session), "")));	            	
	            	
	            }
	            		
	            		
            }
            else if(message.equals("left") && roomList.get(romid).getGaming().getMakedBlock() ) {
	            if(roomList.get(romid).getGaming().getStartCheck()) {
	            	roomList.get(romid).getGaming().leftMove();
	            	sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		Session anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "left")));
	            	}
	            	
	            }
            }
            else if(message.equals("right") && roomList.get(romid).getGaming().getMakedBlock() ) {
            	if(roomList.get(romid).getGaming().getStartCheck()) {
            		roomList.get(romid).getGaming().rightMove();
            		sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		Session anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "right")));
	            	}
            		
            	}
            }
            else if(message.equals("convert") && roomList.get(romid).getGaming().getMakedBlock() ) {
            	if(roomList.get(romid).getGaming().getStartCheck())  {        	
            		roomList.get(romid).getGaming().convertBlock();
            		sendMessage(session, makeJson(comParaMeter(session)));
	            	if(roomList.get(romid).anotherUserCheck(session)) {
	            		Session anoSession = roomList.get(romid).anotherUser(session);
	            		sendMessage(anoSession, makeJson(comParaMeter(anoSession, "gaming", "convert")));
	            	}
            		
            	}
            }
            else {

            }     
            
        }

       
    }
    

    /**
     * 웹소켓 사용자 연결 해제하는 경우 호출
     */
    @OnClose
    public void handleClose(Session session) {
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
     * 웹소켓 에러 발생하는 경우 호출
     */
    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }
    
    

    /*플레이어 화면에 데이터 전달*/
    private boolean sendMessageToAll(String message) {
        if (sessionList == null) {
            return false;
        }

        int sessionCount = sessionList.size();
        if (sessionCount < 1) {
            return false;
        }

        Session singleSession = null;

        for (int i = 0; i < sessionCount; i++) {
            singleSession = sessionList.get(i);
            if (singleSession == null) {
                continue;
            }

            if (!singleSession.isOpen()) {
                continue;
            }

            sessionList.get(i).getAsyncRemote().sendText(message);
        }

        return true;
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
			if(roomList.get(romid).getGaming().getStartCheck()) {
				
				//게임 진행중인 방에서 중간에 한명 나갔을경우 게임 end전까지 진행은 되게해야함
				//#방에 인원이 한명인지 두명인지 알아본후 정해야함
				
				//중간에 한명이 나가서 방에서 한명이 게임줄일때
				if(!roomList.get(romid).anotherUserCheck(sessionList.get(k))) {
					
					roomList.get(romid).getGaming().gaming();
					HashMap sendInfo = comParaMeter(sessionList.get(k), "gaming", "");
					sendMessage(sessionList.get(k), makeJson(sendInfo));
					
				}
				//방에 2명이서 플레이 중일경우
				else {

					//room에서 첫번째로 게이밍 체크하는 경우
					if(!roomList.get(romid).isGamingCheck()) {
						//게이밍 연산 실행 및 게임 진행 상태 확인
						boolean gamestatus = roomList.get(romid).getGaming().gaming();
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
	public HashMap sendUserInfo(Session session, String mesg) {
		
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
	public HashMap comParaMeter(Session session) {
		Integer romid = userRoomInfo.get(session);
		
		HashMap curHash = roomList.get(romid).getGaming().getHash();
		curHash.put("block", roomList.get(romid).getGaming().getBlockLoc() );
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
	public HashMap comParaMeter(Session session, String status, String mesg) {
		
		if (session != null) {
		Integer romid = userRoomInfo.get(session);
		
		HashMap curHash = roomList.get(romid).getGaming().getHash();
		curHash.put("block", roomList.get(romid).getGaming().getBlockLoc() );
		curHash.put("score", roomList.get(romid).getGaming().getScore() );
		curHash.put("numOfUser", sessionList.size() );
		curHash.put("nextBlock", roomList.get(romid).getGaming().getNextBlock() );
		
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
	private String makeJson(HashMap data) {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(data);
			return json ;
		} catch (JsonProcessingException e) {
			System.out.println("json 에러");
			return "";
		}
	}
	
	private void sendMessage(Session session, String mesg) {
		if (session != null) {
			if(session.isOpen() && mesg !="" && mesg !=null)
				session.getAsyncRemote().sendText(mesg);
		}
	}
	
	
	
}	

