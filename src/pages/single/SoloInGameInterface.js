import React, { useEffect, useState } from 'react'
import './SoloInGameInterface.css'
import { useLocation } from 'react-router-dom'
import ReactAudioPlayer from 'react-audio-player'
import music1 from '../../assats/music/LOVELOVELOVE편집1.m4a'
// import music1 from 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx'

export default function SoloInGameInterface(props) {
    const url = 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx'
    //TODO axios패키지 설치하여 아래 폼으로 데이터 전송받아 사용하는거 추가 구현
    const singsData = {
        title: 'Love Love Love',
        singer: '에픽하이',
        music: music1,
    }
    const location = useLocation()
    const state = location.state
    const [anser, setAnser] = useState('')
    const [anserData, setAnserData] = useState([])
    const [qustioncoimt, setQustioncoimt] = useState(state.QuestionCount)
    // const [userName, setuserName] = useState('홍길동')
    // const [toDate, setToDate] = useState('2020')
    // const [fromDate, setFromDate] = useState('2021')
    const [time, setTime] = useState(60)
    //todo key값 지정

    useEffect(() => {
        console.log('state', state)
    }, [])

    useEffect(() => {
        const countdown = setInterval(() => {
            if (parseInt(time) > 0) {
                setTime(parseInt(time) - 1)
            }
            if (parseInt(time) == 0) {
                setTime(60)
            }
        }, 1000)
        return () => clearInterval(countdown)
    }, [time])
    //https://gaemi606.tistory.com/entry/React-%ED%83%80%EC%9D%B4%EB%A8%B8-%EB%A7%8C%EB%93%A4%EA%B8%B0-Hooks-setInterval

    //정답 리스트업로드.
    function onClickAnswer() {
        setAnserData((anserData) => [...anserData, anser])
        setAnser('')
        // TODO 정답 입력 추가구현 필요
        if (anser == singsData.title) {
            setTime(60)
            setQustioncoimt(qustioncoimt - 1)
            //     setScore(score+1);
            //     setquestionCount(questionCount-1);
        }
    }
    function onClickGoHome() {
        window.location.href = '/'
    }

    const anserList = anserData.map((data, index) =>
        data ? <div key={index}>{data}</div> : <div>""</div>,
    )

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
                        <ReactAudioPlayer src={url} autoPlay controls />
                    </div>
                </div>
                <div className={'content2'}>
                    <div>
                        <div>노래 가수 힌트</div>
                        <div>노래 초성 힌트</div>
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
                        onKeyPress={(e) =>
                        {
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
