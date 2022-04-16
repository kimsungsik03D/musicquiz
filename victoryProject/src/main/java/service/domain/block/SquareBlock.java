package service.domain.block;
/*     블록 모양
 * 	    **
 * 	    **
 * 
 * */


public class SquareBlock extends ParentBlock implements Block {


	
	private int cloumns = 0;
	
	public SquareBlock(int columns){
		this.cloumns = columns;
	}
	
	@Override
	public boolean createBlock() {
		block.add(new Node(0,3));
		block.add(new Node(0,4));
		block.add(new Node(1,3));
		block.add(new Node(1,4));
		
		return true;
	}
	

		

}
