import React, { useState } from 'react'
import { Link } from 'react-router-dom'

export default function Main() {
    const [userName, setUserName] = useState('')
    function onChangeUserName(e) {
        setUserName(e.target.value)
    }
    return (
        <div>
            <div>메인페이지입니다.</div>
            <div>
                <input type={'text'} name={'username'} onChange={onChangeUserName} />{' '}
            </div>
            <div style={{ display: 'flex' }}>
                <div>
                    <Link to={'SoloGameCreate'} state={{name : userName?userName:''}}>
                        솔로
                    </Link>
                    {/*<br/>*/}
                    {/*<Link to={'SoloGameCreate'}>솔로방만들기(임시)</Link>*/}
                </div>
                <div>
                    <Link to={'#'}>멀티</Link>
                </div>
                <div>
                    <Link to={'#'}>사용자설정</Link>
                </div>
            </div>
            <div>
                <Link to={'SoketTest'}>소켓테스트</Link>
            </div>
        </div>
    )
}
