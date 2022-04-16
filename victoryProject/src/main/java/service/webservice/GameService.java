package service.webservice;

import java.util.HashMap;

import service.domain.redis.RedisGaming;

public interface GameService {

	 boolean gameStart(RedisGaming redisGame);
	 boolean gaming(RedisGaming redisGame);
	 void leftMove(RedisGaming redisGame);
	 void rightMove(RedisGaming redisGame);
	 void convertBlock(RedisGaming redisGame);
	 HashMap getBlockLoc(RedisGaming redisGame);
	 HashMap getNextBlock(RedisGaming redisGame);
	 void endGame(RedisGaming redisGame);
	
	
}
