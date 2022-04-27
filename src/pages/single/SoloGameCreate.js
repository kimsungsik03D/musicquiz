import React, { useEffect, useState } from 'react'
import './SoloInGameCreate.css'
import { Link, useLocation } from 'react-router-dom'

const SoloGameCreate = (props) => {
    const location = useLocation()
    const state = location.state
    const [input, setInput] = useState({
        userName: state.userName?state.userName:'' ,
        toYear: null,
        fromYear: null,
        questionCount: null,
        modtype: '',
        songHint: '',
        singerHint: '',
        rankMod : '',
    })
    //화면에서 사용하기 위한 변수를 선언해 비구조화할당을 통해 해당 변수에 Input 값을 추출한다.
    const { userName, toYear, fromYear, questionCount, singerHint, songHint } = input

    // let ws = null
    // ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
    // ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
    props.props.onopen = (e) => {
        console.log('connected!!')
    }
    props.props.onmessage = (e) => {
        console.log(JSON.parse(e.data))
        console.log('onmessage!!')
    }
    props.props.onclose = (e) => {
        console.log('onclose')
    }

    function onChange(e) {
        const { value, name } = e.target
        // number일경우 형변환
        if(e.target.name=='toYear' || e.target.name=='fromYear' || e.target.name=='questionCount'){
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

    const onClickMessage = (e) => {
        // var msg = {
        //     "userName" : "message",
        //     "toYear" : 2000,
        //     "fromYear" : 2020,
        //     "questionCount" :2 ,
        //     "modtype" :true,
        //     "songHint" : true,
        //     "singerHint" : true,
        //     "rankMod" : true
        // }
        props.props.send(JSON.stringify(input))
    }
    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div onClick={onClickGoHome}>솔로</div>
                <div>{state.userName}</div>
                <div>년도 - 년도</div>
            </div>
            <div className={'container'}>
                <div className={'setting1'}>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input name={'toYear'} type={'text'} placeholder={'초기년도'} size={4} onChange={onChange}  />{' '}-{' '}
                            <input name={'fromYear'} type={'text'} placeholder={'후기년도'} size={4} onChange={onChange} />
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 :{' '}
                            <input type={'radio'} name={'singerHint'} id={'singernameonpen'}  value={true} onClick={onChange} />{' '}공개{' '}
                            <input type={'radio'} name={'singerHint'} id={'singernameoclose'} value={false} onClick={onChange} />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 :{' '}
                            <input type={'radio'} name={'songHint'} id={'singertitleonpen'}  value={true} onClick={onChange} />{' '}
                            공개{' '}
                            <input type={'radio'} name={'songHint'} id={'singertitleoclose'} value={false} onClick={onChange} />{' '}
                            비공개
                        </div>
                        <div>
                            modtype :{' '}
                            <input type={'radio'} name={'modtype'} id={'singertitleonpen'}  value={true} onClick={onChange} />{' '}
                            공개{' '}
                            <input type={'radio'} name={'modtype'} id={'singertitleoclose'} value={false} onClick={onChange} />{' '}
                            비공개
                        </div>
                        <div>
                            rankMod :{' '}
                            <input type={'radio'} name={'rankMod'} id={'singertitleonpen'}  value={true} onClick={onChange} />{' '}
                            공개{' '}
                            <input type={'radio'} name={'rankMod'} id={'singertitleoclose'} value={false} onClick={onChange} />{' '}
                            비공개
                        </div>
                    </div>
                </div>
                <div className={'setting2'}>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                            <input type={'text'} placeholder={'문제 갯수 입력'} name={'questionCount'} onChange={onChange} />{' '}개
                        </div>
                    </div>
                </div>
                <div>
                    <Link to="/SoloInGameInterface" state={input}>
                        {/*TODO onClick 이벤트를 통해 서버로 같이 넘겨주기.*/}
                        <input type={'button'} value={'게임시작'} onClick={onClickMessage} />
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default SoloGameCreate
