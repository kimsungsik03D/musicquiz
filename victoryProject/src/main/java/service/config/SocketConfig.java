package service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import lombok.RequiredArgsConstructor;
import service.web.Intercepter.IpHandshakeInterceptor;
import service.web.handler.MultiHandler;
import service.webservice.MultiGameService;
import service.webservice.RoomService;
//import service.web.WebSocket;
import service.webservice.UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer{

	/* javax websocket를 사용할때 설정 코드
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
	  return new ServerEndpointExporter();
	}

	*/
	
	//솔로모드 서버 설정
    /*private final SoloHandler chatHandler;
     * 
     * 	@Autowired
	public void setGameService(GameService gameService) {
		chatHandler.gameService = gameService ;
	}	
     * 
*/
    //멀티모드 서버 설정
    private final MultiHandler chatHandler;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    
    	/* handler 설정
    	 * crossOrigin 설정
    	 * Interceptor 추가 (Ip address 주소 알아옴)
    	 * *sockJs 추가 예정
    	 * */
        registry.addHandler(chatHandler, "websocket").setAllowedOrigins("*").addInterceptors(new IpHandshakeInterceptor());
    }
    
	@Autowired
	public void setUserService(UserService userService) {
		chatHandler.userService = userService ;
	}
	
	@Autowired
	public void setGameService(MultiGameService gameService) {
		chatHandler.gameService = gameService ;
	}	

	@Autowired
	public void setRoomService(RoomService roomService) {
		chatHandler.roomService = roomService ;
	}	
}
