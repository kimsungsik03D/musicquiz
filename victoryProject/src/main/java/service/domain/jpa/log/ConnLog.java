/*
 * 서버 접속할때의 로그인 로그
 * */

package service.domain.jpa.log;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.domain.jpa.CreateTimeEntity;
import service.domain.jpa.User;

@Getter
@NoArgsConstructor
@Entity
@Table(name="connlog")
public class ConnLog extends CreateTimeEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column()
	String ipAddress ;
	
	@Column()
	private String session ;
	
	@CreatedDate
	private LocalDateTime createdDate;	

	
	@Builder()
	private ConnLog(String ipAddress, String session ) {
		this.ipAddress = ipAddress ;
		this.session = session;
	}
}
