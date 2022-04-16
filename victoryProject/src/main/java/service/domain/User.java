package service.domain;
/*
 * user 정보를 저장하기 위해 만든 객체
 * DB연동할때는 생성날짜까지 추가
 * */

import javax.websocket.Session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class User {

	private Session session ;
	private String id ;
	
	@Setter
	private String name ;
	private String ip_address;
	
}
