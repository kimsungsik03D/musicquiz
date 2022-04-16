package service.webservice;


//게이밍 계산 구현 코드

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Service;

import service.domain.block.Block;
import service.domain.block.Node;
import service.domain.block.ReverseNakeBlock;
import service.domain.block.SnakeBlock;
import service.domain.block.SquareBlock;
import service.domain.block.StickBlock;
import service.domain.block.UBlock;
import service.domain.redis.RedisGaming;

@Service
public class GameServiceImpl implements GameService {
	
	Random random = new Random() ;
	

	
	public boolean gameStart(RedisGaming redisGame) {

		int rows = redisGame.getRows() ;
		int columns = redisGame.getColumns();
		
		//점수판으로 사용되는 hashMap 초기화
		for(int i =0; i<rows; i++) {
			for(int j=0;  j<columns; j++) {
				
				redisGame.getHash().put(i*columns+j, false);
			}
		}
		
		//게임판 초기화후 블록 생성
		makeBlock(redisGame);
		
		redisGame.setMakedBlock(true);
		redisGame.setStartCheck(true);
		
		return true ;
	}
	
	//블럭 만듬 최초 실행이라 nextBlock 정보 없을때
	private boolean makeBlock(RedisGaming redisGame) {
		
		int rand =random.nextInt(5);
		int columns = redisGame.getColumns();
		try {
		if(rand ==0) {
			redisGame.setLaningBlock(new UBlock(columns));
			redisGame.getLaningBlock().createBlock();
			
		}
		else if(rand ==1) {
			redisGame.setLaningBlock(new SquareBlock(columns));
			redisGame.getLaningBlock().createBlock();
		}
		else if(rand ==2) {
			redisGame.setLaningBlock(new StickBlock(columns));
			redisGame.getLaningBlock().createBlock();
		}
		else if(rand ==3) {
			redisGame.setLaningBlock(new SnakeBlock(columns));
			redisGame.getLaningBlock().createBlock();
		}
		else if(rand ==4) {
			redisGame.setLaningBlock(new ReverseNakeBlock(columns));
			redisGame.getLaningBlock().createBlock();
		}
		redisGame.setNextBlock(new UBlock(columns)) ;
		redisGame.getNextBlock().createBlock();
			return true ;
		}
		catch (Exception e) {
			return false;
		}
		
		
		
	}
	
	//next 블록 정보 가지고 있을때
	private boolean makeBlock(Block block, RedisGaming redisGame) {
		
		redisGame.setLaningBlock(block);
			
		int rand =random.nextInt(5);
		int columns = redisGame.getColumns();
		try {
			if(rand ==0) {
				redisGame.setNextBlock(new UBlock(columns));
				redisGame.getNextBlock().createBlock();
			}
			else if(rand ==1) {
				redisGame.setNextBlock(new SquareBlock(columns));
				redisGame.getNextBlock().createBlock();
			}
			else if(rand ==2) {
				redisGame.setNextBlock(new StickBlock(columns));
				redisGame.getNextBlock().createBlock();
			}
			else if(rand ==3) {
				redisGame.setNextBlock(new SnakeBlock(columns));
				redisGame.getNextBlock().createBlock();
			}
			else if(rand ==4) {
				redisGame.setNextBlock(new ReverseNakeBlock(columns));
				redisGame.getNextBlock().createBlock();
			}
			return true ;
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	
	//블록 보드판에 착륙되었는지 확인 착륙시 true , 내려가는중이면 false
	private boolean checkBoard(RedisGaming redisGame) {
		int rows = redisGame.getRows();
		int columns = redisGame.getColumns();
		Block block = redisGame.getLaningBlock();
		for(int i=0; i<block.getBlock().size(); i++) {
			//블록이 마지막 줄에 닿았을때
			
			if(block.getBlock().get(i).getX()*columns + block.getBlock().get(i).getY() > (rows-1)*columns -1) {
				
				for(int j=0; j<block.getBlock().size(); j++) {
					redisGame.getHash().put(block.getBlock().get(j).getX()*columns + block.getBlock().get(j).getY(), true);
				}
				return true ;
			}
			//활성화된 블록일경우 블록 현재 위치 칸들 전부 활성화
			else if(redisGame.getHash().get((block.getBlock().get(i).getX()+1)*columns + block.getBlock().get(i).getY())) {
				for(int j=0; j<block.getBlock().size(); j++) {
					redisGame.getHash().put(block.getBlock().get(j).getX()*columns + block.getBlock().get(j).getY(), true);
				}
				
				return true ;
			}
			
			else {
				
			}
			
		}
		//블록을 내림
		redisGame.getLaningBlock().downBlock();
		//블록이 내려가는중
		return false;
		
	}
	
	//완성된 row가 있는지 체그 (완성된 로우가 있다면 스코어 계산후 모든 활성화 블록 아래도 내림)
	private void rowSuccess(RedisGaming redisGame) {
			
		ArrayList<Integer> successLine = new ArrayList<Integer>();
		int rows = redisGame.getRows();
		int columns = redisGame.getColumns();	
		int score = redisGame.getScore();
		
		//완료된 행수 계산
		for(int i=0; i<rows; i++) {
			boolean check = true ;
			for(int j=0; j<columns; j++) {
				if(!redisGame.getHash().get(j + i*columns)) {
					check = false ;
					break;
					
				}		
			}
			
			//행이 완성될 경우 완료된 행 수 추가
			if(check) 
				successLine.add(i);
							 
		}
		
		//완성된 줄은 인식
		//스코어 반영
		if(successLine.size() ==0) {
			//size 0이면 pass
		}
		else if(successLine.size() ==1) {
			redisGame.setScore(score+1);
			
		}
		else {
			redisGame.setScore(score += Math.pow(2 , successLine.size()));
		}
		
		for(int i : successLine) {
			//int sucRow = successLine.get(i);
			for(int j =0 ; j < columns; j++) {
				redisGame.getHash().put(i*columns+j, false);
			}	 

		}				
		
		
		//완성된 줄이 있다면 활성화 블록들을 아래로 내림
		if(successLine.size() >0) {		
			for(int k :successLine) {
			for(int i=k*columns+columns-1; i>-1; i--) {	 
				//여기도 마지막줄 일떄랑 구분
					if(i > (rows-1)*columns -1 ) {
						
					}
					else if(!redisGame.getHash().get(i+columns).booleanValue() && redisGame.getHash().get(i).booleanValue()) {
						redisGame.getHash().put(i, false);
						redisGame.getHash().put(i+columns, true);
					}
				}
			}
							
		}
		//완성된 줄이 없다면 패스
		else {
			
		}
			
	}
		
	//게임이 끝났는지 확인 끝났으면 true, 안끝났으면 false
	private boolean endCheck(RedisGaming redisGame) {
		int columns = redisGame.getColumns();
		
		for(int i=0; i<columns; i++) {
			if(redisGame.getHash().get(i).booleanValue()) 
				return true;			
		}
		//게임이 안끝남
		return false;
		
	}
	
	
	//webSocket에서 게임 상태를 받아옴
	public boolean gaming(RedisGaming redisGame) {
		//블록이 떨어졌을때
		if(checkBoard(redisGame)) {
			redisGame.setMakedBlock(false);
			
			rowSuccess(redisGame);
			if(endCheck(redisGame)) return false;
			else {
				//게임이 안끝났다면 신규 블록을 생성한다.
				makeBlock(redisGame.getNextBlock(), redisGame);
				redisGame.setMakedBlock(true);
				return true;
			}
			
		}
		
		//블록이 떨어지는중
		else {
			
			return true ;
		}
	}

	//블록 left 이동 이벤트
	public void leftMove(RedisGaming redisGame) {
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		int rows = redisGame.getRows();
		int columns = redisGame.getColumns();	
		
		
		for(Node n : redisGame.getLaningBlock().priorLeftMoveBlock()) {
			if(n.getY() >columns-1 || n.getY() <0 || n.getY() >rows*columns-1  || redisGame.getHash().get((n.getX())*columns+n.getY()) ) {
				possibleCheck =false;
				break;
			}
		}
		//회전해도 되는 경우
		if(possibleCheck) 
			redisGame.getLaningBlock().leftMoveBlock();
		
	}
	
	//블록 right 이동 이벤트
	public void rightMove(RedisGaming redisGame) {
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		int rows = redisGame.getRows();
		int columns = redisGame.getColumns();	
		
		for(Node n : redisGame.getLaningBlock().priorRightMoveBlock()) {
			if(n.getY() >columns-1 || n.getY() <0 || n.getY() >rows*columns-1  || redisGame.getHash().get((n.getX())*columns+n.getY()) ) {
				possibleCheck =false;
				break;
			}
		}
		//회전해도 되는 경우
		if(possibleCheck) 
			redisGame.getLaningBlock().rightMoveBlock();			
	}
	
	//블록 회전 이벤트
	public void convertBlock(RedisGaming redisGame) {
		//블록이 내려가기전 회전이벤트를 발생했나 체크
		boolean firstLine = false;
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		int rows = redisGame.getRows();
		int columns = redisGame.getColumns();	
		
		for(Node n : redisGame.getLaningBlock().getBlock() ) {
			if(n.getX()==0) {
				firstLine = true;
				break;
			}
		}
		
		if(firstLine) {
			// 아직 첫줄일때는 하강하지 않는다.
		}
		else {
		for(Node n : redisGame.getLaningBlock().priorConvertBlock()) {
			
			if((n.getX())*columns+n.getY()> rows*columns-1 ) {
				//laningBlock.backupBlock();
				redisGame.getLaningBlock().reConvert();
				possibleCheck =false;
				break;
			}
			else if(n.getX() <0 || n.getX()*columns+n.getY() <-1  || n.getY() >columns-1 || n.getY() <0 ) {
				//laningBlock.backupBlock();
				redisGame.getLaningBlock().reConvert();
				possibleCheck =false;
				break;
			}
			else if(redisGame.getHash().get((n.getX())*columns+n.getY())) {
				redisGame.getLaningBlock().reConvert();
				possibleCheck =false;
				break;
			}

		}
		
		//회전해도 되는 경우
		if(possibleCheck) 
			redisGame.getLaningBlock().convertBlock();

		}
	}
	
	//현재 블록 위치 알려줌
	public HashMap getBlockLoc(RedisGaming redisGame) {
		HashMap curLoc = new HashMap();
		int columns = redisGame.getColumns();
		int count =0;
		for (Node n :redisGame.getLaningBlock().getBlock()) {
			curLoc.put(count, n.getX()*columns + n.getY());
			count++;
		}
		return curLoc;
	}
	
	//다음 블록 모양 알려줌
	public HashMap getNextBlock(RedisGaming redisGame) {
		HashMap nextLoc = new HashMap();
		int columns = redisGame.getColumns();
		int count =0;
		for (Node n :redisGame.getNextBlock().getBlock()) {
			nextLoc.put(count, n.getX()*columns + n.getY());
			count++;
		}
		return nextLoc;
	}
	
	//셰션 종료등으로 게임 강제 종료
	public void endGame(RedisGaming redisGame) {
		redisGame.setStartCheck(false);
		
	}
	
	
	
}
