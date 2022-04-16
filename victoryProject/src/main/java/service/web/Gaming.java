//실제 게이밍 구현
//소켓 관리 부분에서 게이밍 객체를 session 아이디와 함께 리스트에 저장하도록함

package service.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import service.domain.block.Block;
import service.domain.block.Node;
import service.domain.block.ReverseNakeBlock;
import service.domain.block.SnakeBlock;
import service.domain.block.SquareBlock;
import service.domain.block.StickBlock;
import service.domain.block.UBlock;

public class Gaming {
	
	Random random = new Random() ;
	
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
	

	public Block getBlockInfo() {
		return laningBlock; 
	}
	
	
	public HashMap<Integer, Boolean> getHash() {
		return hash;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public boolean getStartCheck() {
		return startCheck;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean getMakedBlock() {
		return makedBlock;
	}
	
	public boolean gameStart() {
	
		//점수판으로 사용되는 hashMap 초기화
		for(int i =0; i<rows; i++) {
			for(int j=0;  j<columns; j++) {
			
				hash.put(i*columns+j, false);
			}
		}
		
		//게임판 초기화후 블록 생성
		makeBlock();
		
		makedBlock = true;
		startCheck = true;
		
		return true ;
	}
	
	private boolean makeBlock() {
		
		int rand =random.nextInt(5);
		
		try {
		if(rand ==0) {
			laningBlock = new UBlock(columns) ;
			laningBlock.createBlock();
		}
		else if(rand ==1) {
			laningBlock  = new SquareBlock(columns);
			laningBlock.createBlock();
		}
		else if(rand ==2) {
			laningBlock  = new StickBlock(columns);
			laningBlock.createBlock();
		}
		else if(rand ==3) {
			laningBlock  = new SnakeBlock(columns);
			laningBlock.createBlock();
		}
		else if(rand ==4) {
			laningBlock = new ReverseNakeBlock(columns);
			laningBlock.createBlock();
		}
		nextBlock =  new UBlock(columns) ;
		nextBlock.createBlock();
			return true ;
		}
		catch (Exception e) {
			return false;
		}
		
		
		
	}
	
	private boolean makeBlock(Block block) {
		
		laningBlock = block;
			
		int rand =random.nextInt(5);
		try {
		if(rand ==0) {
			nextBlock = new UBlock(columns) ;
			nextBlock.createBlock();
		}
		else if(rand ==1) {
			nextBlock  = new SquareBlock(columns);
			nextBlock.createBlock();
		}
		else if(rand ==2) {
			nextBlock  = new StickBlock(columns);
			nextBlock.createBlock();
		}
		else if(rand ==3) {
			nextBlock  = new SnakeBlock(columns);
			nextBlock.createBlock();
		}
		else if(rand ==4) {
			nextBlock = new ReverseNakeBlock(columns);
			nextBlock.createBlock();
		}
			return true ;
		}
		catch (Exception e) {
			return false;
		}
		
	}
	
	
	//블록 보드판에 착륙되었는지 확인 착륙시 true , 내려가는중이면 false
	private boolean checkBoard(Block block) {
		
		for(int i=0; i<block.getBlock().size(); i++) {
			//블록이 마지막 줄에 닿았을때
			
			if(block.getBlock().get(i).getX()*columns + block.getBlock().get(i).getY() > (rows-1)*columns -1) {
				
				for(int j=0; j<block.getBlock().size(); j++) {
					hash.put(block.getBlock().get(j).getX()*columns + block.getBlock().get(j).getY(), true);
				}
				return true ;
			}
			//활성화된 블록일경우 블록 현재 위치 칸들 전부 활성화
			else if(hash.get((block.getBlock().get(i).getX()+1)*columns + block.getBlock().get(i).getY())) {
				for(int j=0; j<block.getBlock().size(); j++) {
					hash.put(block.getBlock().get(j).getX()*columns + block.getBlock().get(j).getY(), true);
				}
				
				return true ;
			}
			
			else {
				
			}
			
		}
		//블록을 내림
		block.downBlock();
		//블록이 내려가는중
		return false;
		
	}
	
	//완성된 row가 있는지 체그 (완성된 로우가 있다면 스코어 계산후 모든 활성화 블록 아래도 내림)
	private void rowSuccess() {
			
		ArrayList<Integer> successLine = new ArrayList<Integer>();
		
		//완료된 행수 계산
		for(int i=0; i<rows; i++) {
			boolean check = true ;
			for(int j=0; j<columns; j++) {
				if(!hash.get(j + i*columns)) {
					check = false ;
					break;
					
				}		
			}
			
			//행이 완성될 경우 완료된 행 수 추가
			if(check) {
				successLine.add(i);
				
			}
					 
		}
		
		//완성된 줄은 인식
		//스코어 반영
		if(successLine.size() ==0) {
			
		}
		else if(successLine.size() ==1) {
			score += 1;
		}
		else {
			score += Math.pow(2 , successLine.size());
		}
		
		for(int i : successLine) {
			//int sucRow = successLine.get(i);
			for(int j =0 ; j < columns; j++) {
				hash.put(i*columns+j, false);
			}	 

		}				
		
		
		//완성된 줄이 있다면 활성화 블록들을 아래로 내림
		if(successLine.size() >0) {		
			for(int k :successLine) {
			for(int i=k*columns+columns-1; i>-1; i--) {	 
				//여기도 마지막줄 일떄랑 구분
					if(i > (rows-1)*columns -1 ) {
						
					}
					else if(!hash.get(i+columns).booleanValue() && hash.get(i).booleanValue()) {
						hash.put(i, false);
						hash.put(i+columns, true);
					}
				}
			}
							
		}
		//완성된 줄이 없다면 패스
		else {
			
		}
			
	}
		
	//게임이 끝났는지 확인 끝났으면 true, 안끝났으면 false
	private boolean endCheck() {
		for(int i=0; i<columns; i++) {
			if(hash.get(i).booleanValue()) 
				return true;			
		}
		//게임이 안끝남
		return false;
		
	}
	
	//컨트롤러로 받은 블럭 조작 신호를 소켓에 전달하는 함수
	public String sendClientSign(String str) {
		
		return str ;
	}
	
	
	//webSocket에서 게임 상태를 받아옴
	public boolean gaming() {
		//블록이 떨어졌을때
		if(checkBoard(laningBlock)) {
			makedBlock = false;
			rowSuccess();
			if(endCheck()) return false;
			else {
				//게임이 안끝났다면 신규 블록을 생성한다.
				makeBlock(nextBlock);
				makedBlock = true;
				return true;
			}
			
		}
		
		//블록이 떨어지는중
		else {
			
			return true ;
		}
	}

	//블록 left 이동 이벤트
	public void leftMove() {
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		
		for(Node n : laningBlock.priorLeftMoveBlock()) {
			if(n.getY() >columns-1 || n.getY() <0 || n.getY() >rows*columns-1  || hash.get((n.getX())*columns+n.getY()) ) {
				possibleCheck =false;
				break;
			}
		}
		//회전해도 되는 경우
		if(possibleCheck) 
			laningBlock.leftMoveBlock();
		
	}
	
	//블록 right 이동 이벤트
	public void rightMove() {
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		
		for(Node n : laningBlock.priorRightMoveBlock()) {
			if(n.getY() >columns-1 || n.getY() <0 || n.getY() >rows*columns-1  || hash.get((n.getX())*columns+n.getY()) ) {
				possibleCheck =false;
				break;
			}
		}
		//회전해도 되는 경우
		if(possibleCheck) 
			laningBlock.rightMoveBlock();			
	}
	
	//블록 회전 이벤트
	public void convertBlock() {
		//블록이 내려가기전 회전이벤트를 발생했나 체크
		boolean firstLine = false;
		//prior 메서드를 호출해서 변환해도 되는지 먼저 체크
		boolean possibleCheck = true;
		for(Node n : laningBlock.getBlock() ) {
			if(n.getX()==0) {
				firstLine = true;
				break;
			}
		}
		
		if(firstLine) {
			// 아직 첫줄일때는 하강하지 않는다.
		}
		else {
		for(Node n : laningBlock.priorConvertBlock()) {
			
			if((n.getX())*columns+n.getY()> rows*columns-1 ) {
				//laningBlock.backupBlock();
				laningBlock.reConvert();
				possibleCheck =false;
				break;
			}
			else if(n.getX() <0 || n.getX()*columns+n.getY() <-1  || n.getY() >columns-1 || n.getY() <0 ) {
				//laningBlock.backupBlock();
				laningBlock.reConvert();
				possibleCheck =false;
				break;
			}
			else if(hash.get((n.getX())*columns+n.getY())) {
				laningBlock.reConvert();
				possibleCheck =false;
				break;
			}

		}
		
		//회전해도 되는 경우
		if(possibleCheck) 
			laningBlock.convertBlock();

		}
	}
	
	//현재 블록 위치 알려줌
	public HashMap getBlockLoc() {
		HashMap curLoc = new HashMap();
		int count =0;
		for (Node n :laningBlock.getBlock()) {
			curLoc.put(count, n.getX()*columns + n.getY());
			count++;
		}
		return curLoc;
	}
	
	//다음 블록 모양 알려줌
	public HashMap getNextBlock() {
		HashMap nextLoc = new HashMap();
		int count =0;
		for (Node n :nextBlock.getBlock()) {
			nextLoc.put(count, n.getX()*columns + n.getY());
			count++;
		}
		return nextLoc;
	}
	
	//셰션 종료등으로 게임 강제 종료
	public void endGame() {
		startCheck =false ;
	}
	
	
	
}
