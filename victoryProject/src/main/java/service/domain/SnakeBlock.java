package service.domain;
/*     블록 모양
 * 		**
 * 	   **
 * 
 * */


import java.util.List;

public class SnakeBlock extends ParentBlock implements Block {
	private int convertCount = 0;
	private int cloumns = 0;
	
	
	public SnakeBlock(int columns){
		this.cloumns = columns;
		
	}
	
	@Override
	public boolean createBlock() {
		block.add(new Node(0,3));
		block.add(new Node(0,2));
		block.add(new Node(1,2));
		block.add(new Node(1,1));
		
		return true;
	}
	
	@Override
	public List<Node> convertBlock() {
		
		block.clear();
		for(Node n : convertBlock )
			block.add(new Node(n.getX(), n.getY()));
		 
		return block ;
	
	}
	
	@Override
	public List<Node> priorConvertBlock() {
		convertBlock.clear();
		for(Node n : block )
			convertBlock.add(new Node(n.getX(), n.getY()));
		
		return convertBlock(convertBlock) ;
	}
	
	private List<Node> convertBlock(List<Node> blocks) {

		if(convertCount ==0)
			convertCount+=1;
		
		if(convertCount ==1) {
			
			
			blocks.get(0).setX(blocks.get(0).getX()+2);
			blocks.get(1).setX(blocks.get(1).getX()+1);
			blocks.get(1).setY(blocks.get(1).getY()+1);
			blocks.get(3).setX(blocks.get(3).getX()-1);
			blocks.get(3).setY(blocks.get(3).getY()+1);
			convertCount++;
		}
		else if(convertCount ==2) {
			
			blocks.get(0).setY(blocks.get(0).getY()-2);
			blocks.get(1).setX(blocks.get(1).getX()+1);
			blocks.get(1).setY(blocks.get(1).getY()-1);
			blocks.get(3).setX(blocks.get(3).getX()+1);
			blocks.get(3).setY(blocks.get(3).getY()+1);
			convertCount++;
		}
		else if(convertCount ==3) {
			blocks.get(0).setX(blocks.get(0).getX()-2);
			
			blocks.get(1).setX(blocks.get(1).getX()-1);
			blocks.get(1).setY(blocks.get(1).getY()-1);
			blocks.get(3).setX(blocks.get(3).getX()+1);
			blocks.get(3).setY(blocks.get(3).getY()-1);
			
			convertCount++;
		}
		else if(convertCount ==4) {
			
			blocks.get(0).setY(blocks.get(0).getY()+2);
			blocks.get(1).setX(blocks.get(1).getX()-1);
			blocks.get(1).setY(blocks.get(1).getY()+1);
			blocks.get(3).setX(blocks.get(3).getX()-1);
			blocks.get(3).setY(blocks.get(3).getY()-1);
			convertCount=1;
		}

		
		return blocks ;
	}
	
	//회전 실패시 convertCount 되돌림
	@Override
	public void reConvert() {
		if(convertCount == 2 || convertCount == 3 || convertCount ==4  )
			convertCount--;
		else
			convertCount=4;
	}
}
