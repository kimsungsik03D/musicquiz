package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;



@EnableScheduling
@SpringBootApplication()
//JPA Auditing 기능 활성화하여 CreateTime, ModifiedTime를 관리하도록 위임 
@EnableJpaAuditing
public class TetrisApplication  {

	public static void main(String[] args) {
		SpringApplication.run(TetrisApplication.class, args);
		//socketConn();
	}
	

	

	/*초기 소켓 테스트 코드
	//웹 소켓 서버 생성
	public static void socketConn() {
		//웹 소켓 부분
		ServerSocket serverSocket = null ;
		try {
			serverSocket = new ServerSocket(7777);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			try {
				System.out.println("연결요청 대기중");
				Socket socket =serverSocket.accept();
				System.out.println(socket.getInetAddress()+"연결요청 들어옴");
				
				OutputStream out = socket.getOutputStream();
				DataOutputStream dos = new DataOutputStream(out);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	*/

}
