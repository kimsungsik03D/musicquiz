package service.domain.redis;

import java.io.Serializable;
import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.domain.block.Block;

@RedisHash(value="game", timeToLive =1800)
@Getter
@Setter
@NoArgsConstructor
public class RedisGaming implements Serializable{
    
    private static final long serialVersionUID = 2327692830319429806L;
	
	@Id
	int id ;
	
	private Block laningBlock ;
	private Block nextBlock ;
	
	//블럭이 생성되었나 체크함 (블럭 생성 안되어있을떄는 블록 움직이는 이벤트 반응안함)
	private boolean makedBlock = false;
	
	//게임판으로 사용됨
	private  HashMap<Integer, Boolean> hash = new HashMap<Integer, Boolean>();
	
	//row 수
	private final int rows = 10;
	
	//columns 수
	private final int columns = 6;
	
	//점수
	private int score = 0;
	
	//websocket에서 게임이 시작여부를 체크할때 사용함 (처음 시작인지 재시작인지 여부를 판단할때 사용한다.)
	private boolean startCheck = false ;
}
