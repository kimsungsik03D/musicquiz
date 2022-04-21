import React, { useEffect, useState } from 'react'
import './SoloInGameInterface.css'
import { useLocation } from 'react-router-dom'
import ReactAudioPlayer from 'react-audio-player'
import { sampledata,sampleserverdata } from './noadd/gameData'
import music1 from '../../assats/music/LOVELOVELOVE편집1.m4a'
// import music1 from 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx'
//TODO : LOGIC  -> create페이지에서 서버에 데이터를 보내면서 화면을 이동하고 inerface화면에서 서버가 던진 데이터를 받아서 초기값 세팅
//TODO 정답란에 문자입력시 서버에 데이터 전송 후 해당값 받기. 이때 state가 false라면 게임 종료 하면서 스코어 , 런닝타임 출력



export default function SoloInGameInterface(props) {
    //TODO axios패키지 설치하여 아래 폼으로 데이터 전송받아 사용하는거 추가 구현
    const state = useLocation().state
    let gameData = {}
    const [answer, setAnswer] = useState('')
    const [answerData, setAnswerData] = useState([])
    const [qustioncoimt, setQustioncoimt] = useState(state.QuestionCount)

    gameData={...sampleserverdata}
    const [time, setTime] = useState(gameData.time)
    if(gameData.setTimeout==0){
        console.log('시간초과로 인한 다음 정보 가져오기.');
    }
    /*TODO 메시지 받았을때*/
    // ws.onmessage=function(e){
    ////     gameData={...JSON.parse(e.data)}
    //     nextdata({...JSON.parse(e.data)})
    // }




    /* 시간초 계산 */
    useEffect(() => {
        const countdown = setInterval(() => {
            if (parseInt(time) > 0) {
                setTime(parseInt(time) - 1)
            }
            if (parseInt(time) == 0) {
                setTime(gameData.time)
                //TODO 시간이 0이면 서버에서 다시 한번 더 정보를 가져와야함
            }
        }, 1000)
        return () => clearInterval(countdown)
    }, [time])
    //https://gaemi606.tistory.com/entry/React-%ED%83%80%EC%9D%B4%EB%A8%B8-%EB%A7%8C%EB%93%A4%EA%B8%B0-Hooks-setInterval

    /* Go Home */
    function onClickGoHome() {
        window.location.href = '/'
    }

    /* 채팅 데이터 출력*/
    const answerList = answerData.map((data, index) =>
        data ? <div key={index}>{data}</div> : <div>""</div>,
    )
    /*정답 클릭시 호출함수*/
    function onClickAnswer() {
        setAnswerData((answerData) => [...answerData, answer])
        console.log("입력한 정답은 : ",answer,'입니다.');
        // TODO 서버에 데이터 전송하기.
        // ws.send(JSON.stringify({ answer:answer }))
        setAnswer('')
    }
    //정답을 보내고 이후 로직.
    function nextdata(responseData){

        if(responseData.gaming==false){
            console.log("Game over 선언");
            console.log("모달창 통해서 score랑 runningtime 출력");
        }
        else{
            if(responseData.answerCheck==true){
                console.log('정답이므로 게임 정보 세팅');
                console.log("정답입니다 라는 메시지 출력");
                setQustioncoimt(qustioncoimt-1)
                gameData={...responseData}
            }else{
                console.log('오답이므로 게임정보 유지')
                console.log("오답입니다 라는 메시지 출력");
            }
        }
    }
    /* 화면로딩시 1회만 소켓 통신 */
    useEffect((e) => {
        console.log('state', state)
        //TODO 소켓통신을 활요해서 화면이 마운트되면 1회 데이터 정보 받아오기.
    },[])

    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div onClick={onClickGoHome}>솔로</div>
                <div>{state.name}</div>
                <div>
                    {state.toDate} - {state.fromDate}
                </div>
            </div>
            <div className={'container'}>
                <div className={'content1'}>
                    <div>노래정답</div>
                    {/*음량 은 0~1.0 사이값으로 지정.*/}
                    {/*https://www.npmjs.com/package/react-audio-player*/}
                    <div>
                        <ReactAudioPlayer src={gameData.songUrl} autoPlay controls loop/>
                    </div>
                </div>
                <div className={'content2'}>
                    <div>
                        <div>{gameData.singerHint}</div>
                        <div>{gameData.songHint}</div>
                    </div>
                    <div>
                        점수판(score Board)
                        <br />
                        맞춘점수 : {gameData.score} 개 <br /> 남은문제 {`${qustioncoimt} / ${state.questionCount}`}
                    </div>
                </div>
                <div className={'content3'}>
                    <div>입력한 정답 목록 {answerList}</div>
                    <div>
                        <div>{time} 초</div>
                        {/*<div className={gameData.answerCheck?'success':'fail'}>{gameData.answerCheck?"정답입니다":"오답입니다."}</div>*/}
                        {/*css에서 문구 제어*/}
                        <div className={gameData.answerCheck?'success':'fail'}/>
                    </div>
                </div>
                <div className={'content4'}>
                    <input type="text" id="answer" placeholder="정답이력란" value={answer}
                        onChange={(e) => {
                            setAnswer(e.target.value)
                        }}
                        onKeyPress={(e) => {
                            if (e.key == 'Enter') {
                                onClickAnswer()
                            }
                        }}
                    />
                    <input type="button" value="입력" onClick={onClickAnswer} />
                </div>
            </div>
        </div>
    )
}
