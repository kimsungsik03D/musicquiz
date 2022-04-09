import React, { useState } from 'react'
import './SoloInGameCreate.css'
import { Link } from 'react-router-dom'

const SoloGameCreate = () => {
    const [userName, setuserName] = useState('홍길동')
    const [toDate, setToDate] = useState('2020')
    const [fromDate, setFromDate] = useState('2021')
    const [QuestionCount, setQuestionCount] = useState(60)

    function onClickGoHome() {
        window.location.href = '/'
    }

    function onClickNameOpen(e) {
        console.log(e.target.value)
    }
    function onChangeQuestionCount(e) {
        setQuestionCount(e.target.value)
    }
    function onChangeonDate(e) {
        setToDate(e.target.value)
    }
    function onChangefromDate(e) {
        setFromDate(e.target.value)
    }
    return (
        <div className={'mainContainer'}>
            <div className={'SoloHeader'}>
                <div onClick={onClickGoHome}>솔로</div>
                <div>사용자이름</div>
                <div>년도 - 년도</div>
            </div>
            <div className={'container'}>
                <div className={'setting1'}>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input type={'text'} placeholder={'초기년도'} size={4} onChange={onChangeonDate}/> -{' '}
                            <input type={'text'} placeholder={'후기년도'} size={4} onChange={onChangefromDate}/>
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 :{' '}
                            <input type={'radio'} name={'nameopen'} onClick={onClickNameOpen} />{' '}
                            공개 <input type={'radio'} name={'nameclose'} /> 비공개
                        </div>
                        <div>
                            노래 초성 힌트 : <input type={'radio'} name={'songdopen'} /> 공개{' '}
                            <input type={'radio'} name={'sondgclose'} /> 비공개
                        </div>
                    </div>
                </div>
                <div className={'setting2'}>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                            <input
                                type={'text'}
                                placeholder={'문제 갯수 입력'}
                                name={'QuestionCount'}
                                onChange={onChangeQuestionCount}
                            />{' '}
                            개
                        </div>
                    </div>
                </div>
                <div>
                    <Link
                        to="/SoloInGameInterface"
                        state={{
                            name : userName,
                            toDate: toDate,
                            fromDate: fromDate,
                            QuestionCount: QuestionCount,
                        }}>
                        <input type={'button'} value={'게임시작'} />
                    </Link>
                </div>
            </div>
        </div>

        //////////////////////////////////////////////////////////////////

        /*       <div>
            <div>
                <div>
                    <div>솔로</div>
                    <div>사용자이름</div>
                    <div>
                        {/!*아마 이 두개는 삭제 가능하지 않을까?*!/}
                        <span>문제년도</span>
                        <span>문제년도</span>
                    </div>
                </div>
                <div>
                    <div>
                        <div>노래년도 설정</div>
                        <div>
                            <input type={'text'} placeholder={'초기년도'} /> -{' '}
                            <input type={'text'} placeholder={'후기년도'} />
                        </div>
                    </div>
                    <div>
                        <div>힌트 설정</div>
                        <div>
                            노래 가수 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                        <div>
                            노래 초성 힌트 : <input type={'radio'} /> 공개 <input type={'radio'} />{' '}
                            비공개
                        </div>
                    </div>
                    <div>
                        <div>문제 개수 설정</div>
                        <div>
                          <input type={'text'} placeholder={'문제 갯수 입력'} /> 개
                        </div>
                    </div>
                  <div>
                    <input type={'button'} value={'게임시작'}/>
                  </div>
                </div>
            </div>
        </div>*/
    )
}

export default SoloGameCreate
