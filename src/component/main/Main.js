import React from 'react'
import { Link } from 'react-router-dom'

export default function Main() {
    return (
        <div>
            <div>메인페이지입니다.</div>
            <div>
                <input type={'text'} />{' '}
            </div>
            <div style={{ display: 'flex' }}>
                <div>
                    <Link to={'SoloInGameInterface'}>솔로</Link>
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
