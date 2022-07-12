import './App.css';

import Main from './pages/main/Main';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';
import SoloGameCreate from './pages/single/SoloGameCreate';
import SoloInGameInterface from './pages/single/SoloInGameInterface';
import MultiGameCreate from './pages/multiple/MultiGameCreate';
import MultiInGameInterface from './pages/multiple/MultiInGameInterface';
import SoketTest from './pages/single/SoketTest';

//테스트용 import
/*import SoketTest from './pages/single/SoketTest';
import SocketTest2 from './pages/single/noadd/SocketTest2';
import TestPage from './pages/single/noadd/testPage';*/

function App() {
  let ws_multi = null
  let ws_solo = null
  ws_multi = new WebSocket(`ws://52.79.186.223:8088/websocket`)
  ws_solo = new WebSocket(`ws://52.79.186.223:8087/websocket`)//ip : Version 3
  // ws = new WebSocket(`ws://3.39.223.71:8088/websocket`)   //ip : Version 2
  // ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
  // ws = new WebSocket(`ws://13.125.191.54:9080/websocket`)

  ws_multi.onopen = (e) => {
    console.log('ws_multi connected!!')
  }
  ws_multi.onerror=(e)=>{

  }
  ws_solo.onopen = (e) => {
    console.log('ws_solo connected!!')
  }
  ws_solo.onerror=(e)=>{

  }

  return (
   <Router>
     <Routes>
       <Route path="/" element={<Main props={ws_multi}/>} />
       <Route path="/SoloInGameInterface" element={<SoloInGameInterface props={ws_solo}/>} />
       <Route path="/SoloGameCreate" element={<SoloGameCreate props={ws_solo}/>} />
       <Route path="/MultiGameCreate" element={<MultiGameCreate props={ws_multi} />} />
       <Route path="/MultiInGameInterface" element={<MultiInGameInterface  props={ws_multi} />} />


       {/*테스트용 path*/}
       <Route path="/SoketTest" element={<SoketTest props={ws_solo}/>} />
       {/*<Route path="/SoketTest2" element={<SocketTest2 />} />*/}
       {/*<Route path="/TestPage" element={<TestPage />} />*/}
     </Routes>

   </Router>
  );
}

export default App;
