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

import service.web.Gaming;
import service.webservice.GameService;
import service.webservice.UserService;

import java.lang.NullPointerException;

@Component
public class SoloHandler extends TextWebSocketHandler  {

	private static List<WebSocketSession> sessionList = new ArrayList<>();
	private static HashMap<String,Gaming> gameMap = new HashMap<String,Gaming>();
	
    /**
     * 서버에 접속한 웹소캣별 게이밍 진행상태 저장
     */
    public static UserService userService ;
    public static GameService gameService;
    
	/*client가 서버에게 메시지 보냄*/


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	try {
    	
    	String payload = message.getPayload();
       
        JSONObject obj = jsonToObjectParser(payload);
        HashMap result = new HashMap();
        //System.out.println(obj);
        if (session != null) {
  
        	//회원정보 추가일시
        	if(obj.get("userName") != null) {
        		gameMap.put(session.getId(), gameService.gameStart(session.getId(), obj));
	
        		Gaming redisGame = gameMap.get(session.getId());
        		
        		int songCountCheck = gameService.songCountCheck(redisGame);
        		//요청 노래숫자가 많을시 에러 메시지 출력 -1은 게임 정상적으로 진행한다임
        		if(songCountCheck==-1 ) {
            		result.put("gaming", true);
            		result.put("songHint", "");
            		result.put("singerHint", "");
            		result.put("songUrl", redisGame.getUri());
            		result.put("time", 30);
        		}
        		else {
            		result.put("stat", false);
            		result.put("songCount", songCountCheck);
            		result.put("msg", "현재 DB 노래숫자가 요청하신 노래수보다 부족합니다");
            		gameMap.put(session.getId(),null);
        		}


        	}
        		
        	else {
        		Gaming redisGame = gameMap.get(session.getId());
        		boolean answerCheck = gameService.answerCheck((String) obj.get("answer"), redisGame);
        		boolean endCheck = gameService.gameCtrl((String) obj.get("answer"), redisGame);
        		
        		//end면 게임 끝
        		if(!endCheck) {
        			//System.out.println("엔드임");
        			
        			if(redisGame.isRankMod()) {
        				gameService.rankSave(redisGame, session);
        			}
        			
        			
        			result.put("gaming", false);
        			result.put("score", redisGame.getScore());
        			result.put("runningTime", redisGame.getClearTime());
        			sendMessage(session, makeJson(result));
        			
        			gameMap.put(session.getId(),null);
        			
        		}
        		
        		//게임 진행중일시
        		else {
        			
        			result.put("gaming", true);
        			result.put("time", redisGame.getRemainTime());
        			
        			//정답이 맞았나 체크
        			if(answerCheck) {
        				result.put("answerCheck", true);
        				result.put("songUrl", redisGame.getUri());
        				result.put("score", redisGame.getScore());
        				sendMessage(session, makeJson(result));
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

 
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

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
