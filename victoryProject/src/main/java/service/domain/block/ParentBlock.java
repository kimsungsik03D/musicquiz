package service.domain.block;
/*
 * 모든 블럭의 공통 조상이다
 * down, left, right move는 공통으로 사용되고
 * 도형 회전(convert)는 각블록마다 Override해서 만들어 사용한다.
 * 
 * 안정성을 위해 설계가 수정되어 Backup은 현재 사용되지 않도록 수정되었다.
 * 
 * */




import java.util.ArrayList;
import java.util.List;

public class ParentBlock implements Block{

	protected  ArrayList<Node> block = new ArrayList<Node>();
	
	//돌렸을때 정합성 체크하기 위한 convert 블록
	protected ArrayList<Node> convertBlock = new ArrayList<Node>() ;
	
	
	public List<Node> getBlock() {
		// TODO Auto-generated method stub
		return block ;
	}

	
	//자식 클래스마다 Override로 생성해 사용
	public boolean createBlock() {
		
		return false;
	}

	public List<Node> convertBlock() {
		return block ;
	}

	
	public List<Node> downBlock() {
		for(Node n : block)
			n.setX(n.getX()+1);
		
		return block ;
	}

	
	public List<Node> leftMoveBlock() {
		block.clear();
		for(Node n : convertBlock )
			block.add(new Node(n.getX(), n.getY()));
				
		return block ;
	}

	
	public List<Node> rightMoveBlock() {
		block.clear();
		for(Node n : convertBlock )
			block.add(new Node(n.getX(), n.getY()));
				
					
		return block ;
	}

	//gaming 객체에서 블럭 움직이는 이벤트 호출하기전 먼저 호출하여 확인
	public List<Node> priorLeftMoveBlock() {
		convertBlock.clear();
		for(Node n : block )
			convertBlock.add(new Node(n.getX(), n.getY()));
		
		for(int i=0; i<convertBlock.size(); i++) {
			convertBlock.get(i).setY(convertBlock.get(i).getY()-1);
		}
		
		return convertBlock ;
	}
	
	public List<Node> priorRightMoveBlock() {
		convertBlock.clear();
		for(Node n : block )
			convertBlock.add(new Node(n.getX(), n.getY()));
		
		
			for(int i=0; i<convertBlock.size(); i++) 
				convertBlock.get(i).setY(convertBlock.get(i).getY()+1);
				
					
		return convertBlock ;
	}
	
	public List<Node> priorConvertBlock() {
		convertBlock.clear();
		return convertBlock ;
	}
	
	
	public void backupBlock() {
		System.out.println(block.get(0).getX() +"," + block.get(0).getY());
		System.out.println(block.get(1).getX() +"," + block.get(1).getY());
		System.out.println(block.get(2).getX() +"," + block.get(2).getY());
		System.out.println(block.get(3).getX() +"," + block.get(3).getY());
		block.clear();
		
		for(Node n : convertBlock ) 
			block.add(new Node(n.getX(), n.getY()));
		
		System.out.println(block.get(0).getX() +",q" + block.get(0).getY());
		System.out.println(block.get(1).getX() +"," + block.get(1).getY());
		System.out.println(block.get(2).getX() +"," + block.get(2).getY());
		System.out.println(block.get(3).getX() +"," + block.get(3).getY());
	}
	
	public void reConvert() {
		
	}

}
