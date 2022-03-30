import React, { useEffect, useRef } from 'react';
//
// const WebSocketContext = React.createContext(null)
// export {WebSocketContext}

export default function SoketTest(children) {
  let ws=null
  useEffect(()=>{
    console.log("123525!!");
    // ws = new WebSocket(`ws://3.35.55.132:8080/`)
    ws = new WebSocket(`ws://3.35.55.132:8081/websocket`)
    // ws = new WebSocket(`ws://localhost/`)
    ws.onopen = (e) =>{
      console.log('e',e);
      console.log("connected!!");
    }
    ws.onmessage = (e) =>{
      console.log('onmessage Event' ,e);
    }
  })

  const sendMessage =() =>{

    ws.send("gello this is client Message")
    console.log('good send');
  }
  return(<div>
    <button  onClick={sendMessage}> 전송</button>
  </div>)

}