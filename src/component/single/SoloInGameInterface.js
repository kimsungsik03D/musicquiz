import React from 'react';
import './SoloInGameInterface.css'

export default function SoloInGameInterface(){
  return(
    <div className={'mainContainer'}>
      <div className={'SoloHeader'}>
        <div>솔로</div>
        <div>사용자이름</div>
        <div>문제년도 - 문제년도</div>
      </div>
      <div className={'container'}>
        <div className={'content1'}><div>노래정답</div></div>
        <div className={'content2'}>
          <div>
            <div>노래 가수 힌트</div>
            <div>노래 초성 힌트</div>
          </div>
          <div>점수판(score Board)<br/>맞춘점수 : <br/> 남은문제 n/n</div>
        </div>
        <div className={'content3'}>
          <div>입력한 정답 목록</div>
          <div>남은시간</div>
        </div>
        <div className={'content4'}><input type='text' placeholder='정답이력란'/></div>
      </div>
    </div>
  )
}