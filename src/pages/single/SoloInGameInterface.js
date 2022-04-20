import React, { useEffect, useState } from 'react'
import './SoloInGameInterface.css'
import { useLocation } from 'react-router-dom'
import ReactAudioPlayer from 'react-audio-player'
import music1 from '../../assats/music/LOVELOVELOVE편집1.m4a'
// import music1 from 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx'

export default function SoloInGameInterface(props) {
    //TODO axios패키지 설치하여 아래 폼으로 데이터 전송받아 사용하는거 추가 구현
    const state = useLocation().state
    const gameData = {
        anserCheck: false,
        gaming: true,
        singerHint: 'ㅇㅇㅇ',
        songHint: 'ㅈㅇㄴ',
        songUrl: 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx',
        time: 30,
    }
    const [answer, setAnswer] = useState('')
    const [answerData, setAnwserData] = useState([])
    const [score, setScore] = useState('')
    const [qustioncoimt, setQustioncoimt] = useState(state.QuestionCount)
    const [time, setTime] = useState(gameData.time)

    /* 화면로딩시 1회만 소켓 통신 */
    useEffect(() => {
        console.log('state', state)
    }, [])

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
    const anserList = anserData.map((data, index) =>
        data ? <div key={index}>{data}</div> : <div>""</div>,
    )
    /*정답 클릭시 호출함수*/
    function onClickAnswer() {
        setAnserData((anserData) => [...anserData, anser])
        setAnser('')
    }

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
                        <ReactAudioPlayer src={gameData.songUrl} autoPlay controls />
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
                        맞춘점수 : <br /> 남은문제 {`${qustioncoimt} / ${state.QuestionCount}`}
                    </div>
                </div>
                <div className={'content3'}>
                    <div>입력한 정답 목록 {anserList}</div>
                    <div>{time}</div>
                </div>
                <div className={'content4'}>
                    <input
                        type="text"
                        id="answer"
                        placeholder="정답이력란"
                        value={anser}
                        onChange={(e) => {
                            setAnser(e.target.value)
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
