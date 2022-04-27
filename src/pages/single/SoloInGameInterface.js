import React, { useEffect, useState } from 'react'
import './SoloInGameInterface.css'
import { useLocation } from 'react-router-dom'
import ReactAudioPlayer from 'react-audio-player'
import Modal from'react-modal'
// import { sampledata,sampleserverdata } from './noadd/gameData'
// import music1 from '../../assats/music/LOVELOVELOVE편집1.m4a'
// import music1 from 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx'
//TODO : LOGIC  -> create페이지에서 서버에 데이터를 보내면서 화면을 이동하고 inerface화면에서 서버가 던진 데이터를 받아서 초기값 세팅
//TODO 정답란에 문자입력시 서버에 데이터 전송 후 해당값 받기. 이때 state가 false라면 게임 종료 하면서 스코어 , 런닝타임 출력

export default function SoloInGameInterface(props) {
    const [isOpen,setIsOpen] = useState(false)
    const state = useLocation().state
    const [gameData, setGameData] = useState({})
    const [answer, setAnswer] = useState('')
    const [answerData, setAnswerData] = useState([])
    const [qustioncoimt, setQustioncoimt] = useState(state.questionCount)
    const [time, setTime] = useState(gameData.time)

    props.props.onopen = (e) => {
        console.log('connected!!')
    }
    props.props.onmessage = (e) => {
        // setGameData(JSON.parse(e.data))
        console.log('!!!!!!!!', JSON.parse(e.data))
        // setGameData({
        //     gaming: true,
        //     singerHint: '',
        //     songHint: '',
        //     songUrl: 'https://docs.google.com/uc?export=open&id=1xM0Lh3wy0akEcm4WaSztiVscw5_lwOlh',
        // })
        // songUrl: 'https://docs.google.com/uc?export=open&id=1fN2mEqy2HDeDnRcyRGpEC44W8gdNMl0O'})

        console.log('onmessage!!')
    }
    props.props.onclose = (e) => {
        console.log('onclose', e)
    }

    // 소켓에서 받아오는 모든 메시지는 useEffec를 통해 관리한다.
    //20220426 서버에서 받아오는 URL에 ' 이 포함되어있어서 정상적인 노래 재생만 안되고있음 다만 songURL을 강제적으로 변경해주면 노래 나옴.
    useEffect(() => {
        props.props.onmessage = (e) => {
            setGameData(JSON.parse(e.data))
            /*setGameData({gaming: true,
                singerHint: "",
                songHint: "",
                // songUrl: 'https://docs.google.com/uc?export=open&id=1xM0Lh3wy0akEcm4WaSztiVscw5_lwOlh'
                })
                 songUrl: 'https://docs.google.com/uc?export=open&id=1fN2mEqy2HDeDnRcyRGpEC44W8gdNMl0O'
                 })

            setGameData({
                answerCheck: false,
                gaming: true,
                score: 0,
                singerHint: "er",
                songHint: "wqer",
                // songUrl: 'https://docs.google.com/uc?export=open&id=1xM0Lh3wy0akEcm4WaSztiVscw5_lwOlh'})
                songUrl: "'https://docs.google.com/uc?export=open&id=13dA4Y5DIi70HFaSgECRhMC7yKFFASAIB'",
                time: 18
            })*/

            setTime(JSON.parse(e.data).time)
            console.log('서버 메시지 : ', JSON.parse(e.data))
            if(gameData.gaming==false){

                setIsOpen(true)
                setGameData( {...gameData, songUrl : '' })
                props.props.close()
            }
        }
    }, [props.props.onmessage])

    /* 시간초 계산 */
    useEffect(() => {
        const countdown = setInterval(() => {
            if (parseInt(time) > 0) {
                if (parseInt(time) == 15) {
                    props.props.send(JSON.stringify({ answer: '' }))
                }

                setTime(parseInt(time) - 1)
            }
            if (parseInt(time) == 0) {
                //TODO 시간이 0이면 서버에서 다시 한번 더 정보를 가져와야함
                props.props.send(JSON.stringify({ answer: '' }))
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
        //TODO 스크롤 제어 필요함.

        console.log('입력한 정답은 : ', answer, '입니다.')
        // TODO 서버에 데이터 전송하기.

        /*  var msg = {
            "userName" : "message",
            "toYear" : 2000,
            "fromYear" : 2020,
            "questionCount" :2 ,
            "modtype" :true,
            "songHint" : true,
            "singerHint" : true,
            "rankMod" : true

        }*/
        props.props.send(JSON.stringify({ answer: answer }))
        setAnswerData((answerData) => [...answerData, answer])
        setAnswer('')
    }
    const EndGame= <div>runningTime : {gameData.runningTime}
                    <br/>
                    score : {gameData.score}<br/></div>
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
                        <ReactAudioPlayer src={gameData.songUrl} autoPlay controls loop volume={0.5}/>
                        {/*<ReactAudioPlayer src={'https://docs.google.com/uc?export=open&id=1pyEtoyEPMXp1pdsVW76FzQaxRCEGO2az'} autoPlay controls loop/>*/}
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
                        맞춘점수 : {gameData.score?gameData.score:0} 개 <br /> 남은문제 {`${qustioncoimt} / ${state.questionCount}`}
                    </div>
                </div>
                <div className={'content3'}>
                    <div>입력한 정답 목록 {answerList}</div>
                    <div>
                        <div>{time} 초</div>
                        {/*<div className={gameData.answerCheck?'success':'fail'}>{gameData.answerCheck?"정답입니다":"오답입니다."}</div>*/}
                        {/*css에서 문구 제어*/}
                        <div className={gameData.answerCheck ? 'success' : 'fail'} />
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
                <Modal isOpen={isOpen} ariaHideApp={false}>
                    hi this Is Modal<br/>
                    runningTime : {gameData.runningTime}
                    <br/>
                    score : {gameData.score}<br/>
                    <div onClick={onClickGoHome}>홈으로</div>
                </Modal>
            </div>
            <input type={'button'} onClick={()=> setIsOpen(!isOpen)} value={'모달창'}/>

        </div>
    )
}
