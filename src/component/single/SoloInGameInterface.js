import React, { useEffect, useState } from 'react';
import './SoloInGameInterface.css'

export default function SoloInGameInterface(props){
  const [anser,setAnser] = useState("")
  const [userName,setuserName] = useState("홍길동")
  const [toDate,setToDate] = useState("2020")
  const [fromDate,setFromDate] = useState("2021")
  const [time,setTime] = useState(60);
  //todo key값 지정
  const [anserData, setAnserData] = useState([]);


  useEffect(() => {
    const countdown = setInterval(() => {
      if (parseInt(time) > 0) {
        setTime(parseInt(time) - 1);
      }
      if(parseInt(time)==0){
        setTime(60)
      }
    }, 1000);
    return () => clearInterval(countdown);
  }, [time]);
  //https://gaemi606.tistory.com/entry/React-%ED%83%80%EC%9D%B4%EB%A8%B8-%EB%A7%8C%EB%93%A4%EA%B8%B0-Hooks-setInterval

  function onClickAnswer(){
    setAnserData(anserData =>[...anserData,anser])
    setAnser("")
  }

  const anserList = anserData.map((data)=>data?<div>{data}</div>:<div>""</div>)
  return(
    <div className={'mainContainer'}>
      <div className={'SoloHeader'}>
        <div>솔로</div>
        <div>{userName}</div>
        <div>{toDate} - {fromDate}</div>
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
          <div>입력한 정답 목록 {anserList}
            </div>
          <div>{time}</div>
        </div>
        <div className={'content4'}><input type='text' id='answer' placeholder='정답이력란' value={anser} onChange={(e)=>{setAnser((e.target.value))}}/>
        <input type='button' value='입력' onClick={onClickAnswer}/></div>
      </div>
    </div>
  )
}