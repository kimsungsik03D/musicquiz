package service.webservice;


//게이밍 계산 구현 코드

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Service;

import service.web.Gaming;

@Service
public class GameServiceImpl implements GameService {
	
	

	
	//!dto 추가 작업 해야함
	public boolean gameStart(Gaming redisGame) {

		return true;
	}
	
	//답안 받았을때의 처리
	//한판이 30초라고 가정, 힌트는 절반이상 흘렀을때 제공
	//!리턴 타입 json으로 변경
	public boolean gameCtrl(Gaming redisGame) {
		
		
		
		//정답이 맞았을때
		
		//힌트 줘야하는 시간이면 힌트 제공
		
		//엔드여부 확인후 엔드가 아닐시 다음 노래 주소 제공
		
		return true;
	}
	
}
