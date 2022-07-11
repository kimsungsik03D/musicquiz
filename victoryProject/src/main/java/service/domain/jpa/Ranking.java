package service.domain.jpa;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ranking")
public class Ranking extends CreateTimeEntity {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id ;
	
	@Column()
	private String session ;
	
	@Column()
	private String username ;
	
	@Column()
	private int score;
	
	@Column()
	private int cleartime;
	
	
	@Builder()
	private Ranking(String session, String username, int score, int cleartime  ) {
		this.session = session;
		this.username = username;
		this.score = score;
		this.cleartime = cleartime;
	}
	
	@CreatedDate
	private LocalDateTime createdDate;
	
}
