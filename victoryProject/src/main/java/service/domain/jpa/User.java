package service.domain.jpa;
/*
 * user 정보를 저장하기 위해 만든 객체
 * DB연동할때는 생성날짜까지 추가
 * */

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.socket.WebSocketSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends CreateTimeEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id ;
	
	@Transient
	private WebSocketSession session ;
	
	@Column()
	private String userid ;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	private String userNm;
	
	private String roomId;
	
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public void setUserNm(String userNm) {
		this.userNm = userNm ;
	}	
	
	@Builder()
	private User(WebSocketSession session, String userid ) {
		this.session = session ;
		this.userid = userid ;
		this.userNm = userid;
	}
	

	

}
