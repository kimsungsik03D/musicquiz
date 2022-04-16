/*Block이다.
 * 
 * 
 * */

package service.domain;

import java.util.List;

public interface Block {

	//block 상태 알려줌
	List<Node> getBlock() ;
	
	boolean createBlock();
	
	List<Node> convertBlock();
	
	List<Node> downBlock();
	
	List<Node> leftMoveBlock();
	
	List<Node> rightMoveBlock();
	
	List<Node> priorLeftMoveBlock();
	
	List<Node> priorRightMoveBlock();
	
	List<Node> priorConvertBlock();
	
	 void reConvert() ;
	//버전 수정하면서 안씀
	 void backupBlock() ;
}
