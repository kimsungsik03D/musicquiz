package service.domain.redis;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash(value="socket", timeToLive =1800)
@Getter
@Setter
@NoArgsConstructor
public class RedisUser implements Serializable{
    
    private static final long serialVersionUID = 2327692830319429806L;
 
    @Id
    //private WebSocketSession session;
    private String  session;
    private String userid ;
 
	@Builder()
	private RedisUser(String session, String userid ) {
		this.session = session ;
		this.userid = userid ;
	}


}
