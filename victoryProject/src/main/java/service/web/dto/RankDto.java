package service.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RankDto {

	
	private int rank;
	
	private String username ;
	
	private int score;
	
	private int cleartime;
	
	
	@Builder()
	private RankDto(int rank, String username, int score, int cleartime  ) {
		this.rank = rank;
		this.username = username;
		this.score = score;
		this.cleartime = cleartime;
	}
	
}
