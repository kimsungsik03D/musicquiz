package service.web.handler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import service.domain.jpa.Room;
import service.domain.jpa.User;
import service.web.Gaming;
import service.web.MultiGaming;
import service.webservice.GameService;
import service.webservice.MultiGameService;
import service.webservice.RoomService;
import service.webservice.UserService;

@Component
public class MultiHandler extends TextWebSocketHandler  {

	//서버에 접속한 유저 리스트 
	private static HashMap<String, User> userMap = new HashMap<String, User>();
	private static HashMap<String,MultiGaming> gameMap = new HashMap<String,MultiGaming>();
	private static HashMap<String, Room> roomMap = new HashMap<String, Room>();
	
	private static List<WebSocketSession> sessionList = new ArrayList<WebSocketSession>();
	
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */
    public static UserService userService ;
    public static MultiGameService gameService;
    public static RoomService roomService;
    
	/*client가 서버에게 메시지 보냄*/
    
    //멀티노드로 만든다면 redis에서 room 정보 리스트 수 를 저장하도록 해야함
    private static int nextRoomId = 0;
  

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	try {
    	
    	String payload = message.getPayload();
       
        JSONObject obj = jsonToObjectParser(payload);
        HashMap result = new HashMap();

        //정상적으로 연결되어 있을때만 메시지 처리
        if (session != null) {
        	
        	if(obj.get("roomStatus") != null) {

        		//방생성
        		if(obj.get("roomStatus").equals("create")) {
        			 
        	            String roomId = Integer.toHexString(nextRoomId);
        	            
        	            nextRoomId++;
        	            roomMap.put(roomId, roomService.roomCreate(roomId));
        	            //방에 들어감
        	            roomService.userJoin(roomMap.get(roomId), session.getId());
        	            //생성한 방의 방장이 됨
        	            roomService.ownerSet(roomMap.get(roomId), session.getId());
        	            
                		result.put("result", true);
                		result.put("roomId", roomId);

                		sendMessage(session, makeJson(result));
        		}
        		//방입장
        		else if(obj.get("roomStatus").equals("enter")) {
        			String roomId = (String) obj.get("roomId");
        			
        			if(roomMap.get(roomId)==null) {
        				//없는 방이라는 메시지 전달
        			}
        			else if(roomMap.get(roomId).getUserList().size()>3) {
        				//인원이 가득찬 방이라는 메시지 전달 
        			}
        			
        			else {
   				
        				//방에 들어감
        	            if(roomService.userJoin(roomMap.get(roomId), session.getId())) {
        	            	userMap.get(session.getId()).setRoomId(roomId);
                    		result.put("result", true);
                    		result.put("userId", session.getId());
                    		
            				for (String u : roomMap.get(roomId).getUserList()) 
            					sendMessage(userMap.get(u).getSession(), makeJson(result));

        	            }
        	            else {
                    		result.put("result", false);
            				for (String u : roomMap.get(roomId).getUserList()) 
            					sendMessage(userMap.get(u).getSession(), makeJson(result));
        	            }
        				
        			}
        			
        		}
        		//방삭제
        		else if(obj.get("roomStatus").equals("delete")) {
        			String roomId = (String) obj.get("roomId");

        			if(roomMap.get(roomId).getRoomOwner().equals(session.getId())) {
        				
        				
        				result.put("result", true);
        				for (String u : roomMap.get(roomId).getUserList()) 
        					sendMessage(userMap.get(u).getSession(), makeJson(result));
        				
        				//userMap에 세팅된 룸 Id 초기화
        				for(String u :roomMap.get(roomId).getUserList()) {
        					userMap.get(u).setRoomId(null);
        				}
        				roomMap.get(roomId).getUserList().clear();
        				roomMap.replace(roomId, null);
        				

        					
        					
        			}
        		}   
        		//게임 시작이나 ready start 될시 result에 보내줘야함
        		else if(obj.get("roomStatus").equals("start")) {
        			String roomId = (String) obj.get("roomId");
        			boolean gameStart = roomService.userclickStart(roomMap.get(roomId), session.getId());

            		//요청 노래숫자가 많을시 에러 메시지 출력 -1은 게임 정상적으로 진행한다임
            		
            		//방장일때 start
            		if(roomMap.get(roomId).getRoomOwner().equals(session.getId()) && gameStart) {
            			System.out.println("시작");
                		
        				roomMap.get(roomId).setGaming(gameService.gameStart(session.getId(), obj));
        				
        				//MultiGaming redisGame = gameMap.get(roomId);
        				MultiGaming redisGame = roomMap.get(roomId).getGaming();
                		int songCountCheck = gameService.songCountCheck(redisGame);
                		System.out.println(songCountCheck);
                		if(songCountCheck ==-1 ) {
                    		result.put("gaming", true);
                    		result.put("songHint", "");
                    		result.put("singerHint", "");
                    		result.put("songUrl", redisGame.getUri());
                    		result.put("time", 30);
                    		
                    		
                    		//게임 score 정보 초기화
                    		for (String u : roomMap.get(roomId).getUserList()) {
                    			redisGame.getScore().put(u, 0); 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
                    		}

                		}
                		else {
                    		result.put("stat", false);
                    		result.put("songCount", songCountCheck);
                    		result.put("msg", "현재 DB 노래숫자가 요청하신 노래수보다 부족합니다");
                    		//gameMap.put(session.getId(),null);
                    		roomMap.get(roomId).setGaming(null);
                    		
                    		for (String u : roomMap.get(roomId).getUserList()) 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));

                		}
            		} 
            		else if(roomMap.get(roomId).getRoomOwner().equals(session.getId()) && !gameStart) {
            			System.out.println("못함");
            			result.put("stat", false);
                		result.put("msg", "모든 유저가 Ready가 아님");
                		
                		for (String u : roomMap.get(roomId).getUserList()) 
                			sendMessage(userMap.get(u).getSession(), makeJson(result));                		
            			
            		}
            		//일반 유저일떄 ready나 ready 해제는 룸서비스 userclickStart가 처리
            		else {
            			
            			if(roomMap.get(roomId).getUserReady().contains(session.getId())) {
            				result.put("ready", true);
            				result.put("userId", session.getId());
                    		for (String u : roomMap.get(roomId).getUserList()) 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
            			}
            			else {
            				result.put("ready", false);
            				result.put("userId", session.getId());
                    		for (String u : roomMap.get(roomId).getUserList()) 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));
            			}
            				
            			
            		}
            		
            		       			
        		}
        		else if(obj.get("roomStatus").equals("setting")) {
        			String roomId = (String) obj.get("roomId");
        			
        			//방장일때만 세팅 가능함
        			if(roomMap.get(roomId).getRoomOwner().equals(session.getId())) {
        				roomMap.get(roomId).setGaming(gameService.gameStart(session.getId(), obj));
        				
                		int songCountCheck = gameService.songCountCheck(roomMap.get(roomId).getGaming());
                		//요청 노래숫자가 많을시 에러 메시지 출력 -1은 게임 정상적으로 진행한다임
                		if(songCountCheck!=-1 ) {
                    		result.put("stat", false);
                    		result.put("songCount", songCountCheck);
                    		result.put("msg", "현재 DB 노래숫자가 요청하신 노래수보다 부족합니다");
                    		

                    		for (String u : roomMap.get(roomId).getUserList()) 
                    			sendMessage(userMap.get(u).getSession(), makeJson(result));

                		}        			
        				
        			}
        				
        		}
        		
        	}
        	
        	//게임진행중일때
        	else if(obj.get("roomStatus").equals("gaming")) {
        		String roomId = (String) obj.get("roomId");
        		
        		MultiGaming redisGame = gameMap.get(roomId);
        		boolean answerCheck = gameService.answerCheck((String) obj.get("answer"), redisGame);
        		boolean endCheck = gameService.gameCtrl(session.getId(),(String) obj.get("answer"), redisGame);
        		System.out.println("게임 시작");
        		//end면 게임 끝
        		if(!endCheck) {

        			
        			result.put("gaming", false);
        			result.put("score", redisGame.getScore());
        			result.put("runningTime", redisGame.getClearTime());
        			
        			//방 전체 메시지에 보냄
            		for (String u : roomMap.get(roomId).getUserList()) 
            			sendMessage(userMap.get(u).getSession(), makeJson(result));
            		
        			gameMap.put(roomId,null);
        			
        		}
        		
        		//게임 진행중일시
        		else {
        			System.out.println("게임 진행");
	        		result.put("gaming", true);
	        		result.put("time", redisGame.getRemainTime());
	        			
	        		//정답이 맞았나 체크
	        		if(answerCheck) {
	        				
	        			result.put("answerCheck", true);
	        			result.put("songUrl", redisGame.getUri());
	        			result.put("score", redisGame.getScore());
	        			
	        			//!유저ID에서 이름 전달로 바뀔 수 있음
	        			result.put("userId", session.getId());
	        			
	        			
	                	for (String u : roomMap.get(roomId).getUserList()) 
	                		sendMessage(userMap.get(u).getSession(), makeJson(result));
	                	
	                	
	        			}
	      
	        			//정답이 틀렸을경우 힌드틑 함께 제공해줘야하나 체크
	        			result.put("answerCheck", false);
	        			result.put("songUrl", redisGame.getUri());
	        			result.put("score", redisGame.getScore());
	        				
	            		if(gameService.timeHintCheck(redisGame)) {	
	            			if(redisGame.isSongHintCheck())
	            				result.put("songHint", redisGame.getSongHint());
	            			else
	            				result.put("songHint", "");
	            			if(redisGame.isSingerHinCheckt())
	            				result.put("singerHint", redisGame.getSingerHint());
	            			else
	            				result.put("singerHint", "");
	            		}
	            		else {
	                    	result.put("songHint", "");
	                    	result.put("singerHint", "");		
	            		}
            		
        			
        				
        		}     		

        	}    
        	
        	//클라이언트가 현재 진행상황 요청할때
        	else if(obj.get("roomStatus").equals("progress")) {
        		String roomId = (String) obj.get("roomId");
        		MultiGaming redisGame = gameMap.get(roomId);
        		
        	
    			result.put("score", redisGame.getScore());
    			result.put("questionCount", redisGame.getQuestionCount());
    			result.put("time", redisGame.getRemainTime());
    			
            	for (String u : roomMap.get(roomId).getUserList()) 
            		sendMessage(userMap.get(u).getSession(), makeJson(result));
    			
        	}
        	
        	
        	
        }

        sendMessage(session, makeJson(result));
        
        }
        catch (NullPointerException e) {
        	
        }   
        
     }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    	//접속시 로그 등록
    	userService.addConnLog(session);
    	//전체 세션 리스트에 등록, 방은 아직 배정되지 않음
    	sessionList.add(session);

    	userMap.put(session.getId(), User.builder().session(session)
    												.userid(session.getId())
    														.build());
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	//접속 해제 로그 등록
    	userService.addDisLog(session);
    	//세션 리스트에서 제외
    	sessionList.remove(session);
    }
    
    
    /**
     * 웹소켓 연결된 모든 클라이언트에 1초마다 
		gaming 진행상태 및 블록 렌딩 정보 전달
     */
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

	private void sendMessage(String roomId, TextMessage mesg) {
		if (roomMap.get(roomId) != null && mesg !=null) {
			
			for(String userId : roomMap.get(roomId).getUserList()) {
				try {
					userMap.get(userId).getSession().sendMessage(mesg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	//jsoon 파싱 함수
	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
