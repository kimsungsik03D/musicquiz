package service.web.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomDto {

	private String roomId;
	
	List<String> userList  ;
	
	private boolean startCheck;
	
	private String roomTitle;
	
	
	@Builder()
	private RoomDto(String roomId, List<String> userList, boolean startCheck, String roomTitle  ) {
		this.roomId = roomId;
		this.userList = userList;
		this.startCheck = startCheck;
		this.roomTitle = roomTitle;
	}
	
}
