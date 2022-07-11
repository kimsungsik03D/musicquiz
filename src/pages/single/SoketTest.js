import React, { useEffect, useRef } from 'react'
//
// const WebSocketContext = React.createContext(null)
// export {WebSocketContext}
//TODO : 소켓통신을 위한 로직이기때문에 개발이 완료되면 해당 부분은 필요없는 로직임
export default function SoketTest(props) {
    let ws = null
    console.log('123525!!')
    // ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
    // ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
    props.props.onopen = (e) => {
        console.log('onopen Event', e)
        console.log('connected!!')
    }
    props.props.onmessage = (e) => {
        var receiveData = e.data // 수신 data

        var obj2 = JSON.parse(receiveData)

        console.log(obj2)
        console.log('!!!!!!!!!!!!!1')
    }
    props.props.onclose = (e) => {
        console.log('onclose')
        console.log('onclose e', e)
    }

    const sendMessage = () => {
        var msg = {roomStatus:'create'}
        /*var msg = {
            userName: 'message',
            toYear: 2000,
            fromYear: 2020,
            questionCount: 2,
            modtype: true,
            songHint: true,
            singerHint: true,
            rankMod: true,
        }*/

        props.props.send(JSON.stringify(msg))
        // props.props.onmessage = (evt) => {
        //     console.log('onmessage1')
        //     console.log(evt)
        //     console.log(evt.data)
        // }
    }

    const sendMessage2 = () => {
        var msg = {
            answer: '가나다',
        }
        props.props.send(JSON.stringify(msg))
        props.props.onmessage = (evt) => {
            console.log('onmessage2')
            console.log(evt)
            console.log(evt.data)
        }
        console.log('good send')
    }

    const sendMessage3 = () => {
        props.props.onclose()
    }

    //
    // const sendMessage = () => {
    //   console.log('ws',ws);
    //   var msg = {
    //     userName: 'qqq',
    //     toYear: 2000,
    //     fromYear: 2020,
    //     questionCount: 2,
    //     modtype: true,
    //     songHint: true,
    //     singerHint: true,
    //   }
    //   ws.send(JSON.stringify(msg))
    //   console.log('good send')
    // }
    //
    // const sendAnswer = () => {
    //   console.log('ws',ws);
    //   var msg = {
    //     userName: 'qqq',
    //     toYear: 2000,
    //     fromYear: 2020,
    //     questionCount: 2,
    //     modtype: true,
    //     songHint: true,
    //     singerHint: true,
    //   }
    //   ws.send(JSON.stringify(msg))
    //   console.log('good send')
    // }
    //

    return (
        <div>
            <button onClick={sendMessage}>게임시작</button>
            <button onClick={sendMessage2}>왼쪽이동</button>
            <button onClick={sendMessage3}>닫기</button>
            {/*<button onClick={sendMessage}> 전송</button>*/}
            {/*<button onClick={sendMessage2}> 전송</button>*/}
            {/*<button onClick={disconnect}> 전송</button>*/}
        </div>
    )
}

const gameData = {
    anserCheck: false,
    gaming: true,
    singerHint: 'ㅇㅇㅇ',
    songHint: 'ㅈㅇㄴ',
    songUrl: 'https://docs.google.com/uc?export=open&id=19x2wK4FSk8WS86XD0TWTKx38hgWuO1dx',
    time: 30,
}
