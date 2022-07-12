import React, { useEffect, useState } from 'react';
import './multiInGameCreate.css'
import { Link, useLocation } from 'react-router-dom'

const MultiGameCreate = (props) => {
    const location = useLocation().state
    const [userId , setUserId] = useState();
    // const state = location.state
    // console.log('################ ', location);
    const [answer, setAnswer] = useState('')
    const [gameset,setGameset] = useState(false)
    const [roomId, setRoomId] = useState()
    const [answerData, setAnswerData] = useState([])
    const [gameState,setGamestet] = useState(false)
    const [input, setInput] = useState({
        roomStatus: 'start',
        toYear: null,
        fromYear: null,
        questionCount: null,
        songHint: '',
        singerHint: '',
        roomId : roomId
    })
    //화면에서 사용하기 위한 변수를 선언해 비구조화할당을 통해 해당 변수에 Input 값을 추출한다.
    const {
        userName,
        toYear,
        fromYear,
        questionCount,
        singerHint,
        songHint,
        rankMod,
        modtype,
        roomOpen,
        gameType,
    } = input
    props.props.onopen = (e) => {
        //console.log('multiGameCreact connected!!')
    }
    props.props.onmessage = (e) => {
        let setid = location.usertype =='host'? (roomId ? roomId : JSON.parse(e.data).roomId) : location.roomid
        setRoomId(setid)
        setGamestet(JSON.parse(e.data).ready?true:false)
        setInput({
            ...input,
            roomId: setid,
        })
        if(userId==null){
            setUserId(JSON.parse(e.data).userId)
        }

    }
    // props.props.onclose = (e) => {
    //     console.log('onclose')
    // }

    function onChange(e) {
        const { value, name } = e.target
        // number일경우 형변환
        if (
            e.target.name == 'toYear' ||
            e.target.name == 'fromYear' ||
            e.target.name == 'questionCount'
        ) {
            setInput({
                ...input,
                [name]: Number(value),
            })
        } else {
            // number가 아닐경우 boolean으로 표현하기위해 삼항연산자 활용
            setInput({
                ...input,
                [name]: value == 'true' ? true : false,
            })
        }
    }

    function onClickGoHome() {
        window.location.href = '/'
    }

    function deleteRoom() {
        props.props.send(JSON.stringify({ roomStatus: 'delete', roomId: roomId }))
    }

    const onClickMessage = (e) => {
        if (location.usertype == 'host') {
            props.props.send(
                JSON.stringify({
                    roomStatus: 'start',
                    roomId:roomId,
                    toYear: 2000,
                    fromYear: 2022,
                    questionCount: 5,
                    modtype: true,
                    songHint: true,
                    singerHint: true,
                }),
            )

        } else {
            props.props.send(
              JSON.stringify({
                  roomStatus: 'start',
                  roomId:roomId,
              }),
            )
        }
    }


    const answerList = answerData.map((data, index) =>
        data ? <div key={index}>사용자 :{data} </div> : <div>""</div>,
    )

    function onClickAnswer() {
        //TODO 스크롤 제어 필요함.

       // console.log('입력한 정답은 : ', answer, '입니다.')
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
        setAnswerData((answerData) => [...answerData, answer])
        setAnswer('')
    }
    useEffect(()=>{
        if(location.usertype=='guest'){
            setGameset(!gameset)
        }
        },[])
    const linkHandle = (event) =>{
        if (location.usertype == 'host') {
            props.props.send(
              /*JSON.stringify({
                  roomStatus: 'start',
                  roomId:roomId,
                  toYear: 2000,
                  fromYear: 2022,
                  questionCount: 5,
                  modtype: true,
                  songHint: false,
                  singerHint: true,
              }),*/
              JSON.stringify(input)
            )
        } else {
            props.props.send(
              JSON.stringify({
                  roomStatus: 'start',
                  roomId:roomId,
              }),
            )
        }

        props.props.onmessage=(e)=>{
            setGamestet(JSON.parse(e.data).ready?true:false)
        }

        if(!gameState){
        event.preventDefault()
        }


    }
    const linkSet = () =>{
        return(location.usertype=='host'?<Link to={"/MultiInGameInterface"} state={input} onClick={linkHandle}>
                        {/*TODO onClick 이벤트를 통해 서버로 같이 넘겨주기.*/}
                        {/*<input type={'button'} value={'게임시작'} onClick={onClickMessage} />*/}
                        <input type={'button'} value={'게임시작'} />
                    </Link>:<Link to={"/MultiInGameInterface"} state={{roomId :roomId}} >
                        {/*TODO onClick 이벤트를 통해 서버로 같이 넘겨주기.*/}
                        <input type={'button'} value={'게임시작'}  onClick={onClickMessage} />
                        {/*<input type={'button'} value={'게임시작'}  />*/}
                    </Link>)
    }
    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div onClick={onClickGoHome}>멀티</div>
                {/*<div>{state.userName}</div>*/}
                <div>방 ID : {roomId ? roomId : null}</div>
                <div className={'userlist'}>
                    <div>사용자 A</div>
                    <div>사용자 B</div>
                    <div>사용자 C</div>
                    <div>사용자 D</div>
                </div>
            </div>
            <div className={'container'}>
                <div className={'setting1'}>
                    <div>
                       {/* <div>
                            방 공개설정 :{' '}
                            <input
                                type={'radio'}
                                name={'roomOpen'}
                                id={'roomOpen'}
                                value={true}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            공개{' '}
                            <input
                                type={'radio'}
                                name={'roomOpen'}
                                id={'roomOpen'}
                                value={false}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            비공개
                        </div>*/}
                        <div>
                            게임 방식 설정 :{' '}
                            <input
                                type={'radio'}
                                name={'modtype'}
                                id={'modtype'}
                                value={true}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            년도 지정{' '}
                            <input
                                type={'radio'}
                                name={'modtype'}
                                id={'modtype'}
                                value={false}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            즉흥 설정
                        </div>
                    </div>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                            <input
                                type={'text'}
                                placeholder={'문제 갯수 입력'}
                                name={'questionCount'}
                                onChange={onChange}
                                disabled={gameset}
                            />{' '}
                            개
                        </div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 :{' '}
                            <input
                                type={'radio'}
                                name={'singerHint'}
                                id={'singernameonpen'}
                                value={true}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            공개{' '}
                            <input
                                type={'radio'}
                                name={'singerHint'}
                                id={'singernameoclose'}
                                value={false}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 :{' '}
                            <input
                                type={'radio'}
                                name={'songHint'}
                                id={'singertitleonpen'}
                                value={true}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            공개{' '}
                            <input
                                type={'radio'}
                                name={'songHint'}
                                id={'singertitleoclose'}
                                value={false}
                                onClick={onChange}
                                disabled={gameset}
                            />{' '}
                            비공개
                        </div>
                    </div>
                </div>
                <div
                    className={'setting2'}
                    style={{ display: 'flex', justifyContent: 'space-between' }}>
                    {/*TODO Boolen으로 화면제어 추가하기!*/}
                    {/*<div style={input.modtype ? { display: 'inline-block' } : { display: 'none' }}>*/}
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input
                                name={'toYear'}
                                type={'text'}
                                placeholder={'초기년도'}
                                size={4}
                                onChange={onChange}
                                disabled={gameset}
                            />{' '}
                            -{' '}
                            <input
                                name={'fromYear'}
                                type={'text'}
                                placeholder={'후기년도'}
                                size={4}
                                onChange={onChange}
                                disabled={gameset}
                            />
                        </div>
                    </div>

                    <div>
                        <div className={'content3'}>
                            <div>
                                <p style={{ position: 'sticky' }}>사용자채팅</p> {answerList}
                            </div>
                            <div style={{ display: 'none' }} />
                        </div>
                        <div className={'content4'}>
                            <input
                                type="text"
                                id="answer"
                                placeholder="정답이력란"
                                value={answer}
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
                <div>
                    {linkSet()}
                    <Link to="/">
                        <input type={'button'} value={'방삭제'} onClick={deleteRoom} />
                    </Link>
                    {/*</Link>*/}
                </div>
            </div>
        </div>
    )
}

export default MultiGameCreate
