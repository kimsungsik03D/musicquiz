import React, { useEffect, useState } from 'react'
import './SoloInGameCreate.css'
import { Link, useLocation } from 'react-router-dom'

const SoloGameCreate = () => {
    const location = useLocation()
    const state = location.state
    const [input, setInput] = useState({
        userName: state ? state.name : '',
        toYear: 0,
        fromYear: 0,
        questionCount: 3,
        modtype: true,
        songHint: true,
        singerHint: true,
    })

    //화면에서 사용하기 위한 변수를 선언해 비구조화할당을 통해 해당 변수에 Input 값을 추출한다.
    const { userName, toDate, fromDate, questionCount, singerHint, songHint } = input

    function onChange(e) {
        const { value, name } = e.target
        setInput({
            ...input,
            [name]: value,
        })
    }
    console.log(input)

    function onClickGoHome() {
        window.location.href = '/'
    }


    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div onClick={onClickGoHome}>솔로</div>
                <div>{userName}</div>
                <div>년도 - 년도</div>
            </div>
            <div className={'container'}>
                <div className={'setting1'}>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input name={'toDate'} type={'text'} placeholder={'초기년도'} size={4} onChange={onChange} value={toDate} />{' '}-{' '}
                            <input name={'fromDate'} type={'text'} placeholder={'후기년도'} size={4} onChange={onChange} value={fromDate}
                            />
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 :{' '}
                            <input type={'radio'} name={'singerHint'} id={'singernameonpen'} value={'open'} onClick={onChange} />{' '}공개{' '}
                            <input type={'radio'} name={'singerHint'} id={'singernameoclose'} value={'close'} onClick={onChange} />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 :{' '}
                            <input type={'radio'} name={'songHint'} id={'singertitleonpen'} value={'open'} onClick={onChange} />{' '}
                            공개{' '}
                            <input type={'radio'} name={'songHint'} id={'singertitleoclose'} value={'close'} onClick={onChange} />{' '}
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
                        <input type={'button'} value={'게임시작'} />
                    </Link>
                </div>
            </div>
        </div>
    )
}

export default SoloGameCreate
