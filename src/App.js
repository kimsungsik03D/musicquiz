import './App.css';

import Main from './pages/main/Main';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';
import SoloGameCreate from './pages/single/SoloGameCreate';
import SoloInGameInterface from './pages/single/SoloInGameInterface';
import MultiGameCreate from './pages/multiple/MultiGameCreate';
import MultiInGameInterface from './pages/multiple/MultiInGameInterface';

//테스트용 import
/*import SoketTest from './pages/single/SoketTest';
import SocketTest2 from './pages/single/noadd/SocketTest2';
import TestPage from './pages/single/noadd/testPage';*/

function App() {
  let ws = null
  ws = new WebSocket(`ws://13.125.191.54:8088/websocket`)
  return (
   <Router>
     <Routes>
       <Route path="/" element={<Main />} />
       <Route path="/SoloInGameInterface" element={<SoloInGameInterface props={ws}/>} />
       <Route path="/SoloGameCreate" element={<SoloGameCreate props={ws}/>} />
       <Route path="/MultiGameCreate" element={<MultiGameCreate/>} />
       <Route path="/MultiInGameInterface" element={<MultiInGameInterface/>} />


       {/*테스트용 path*/}
       {/*<Route path="/SoketTest" element={<SoketTest props={ws}/>} />*/}
       {/*<Route path="/SoketTest2" element={<SocketTest2 />} />*/}
       {/*<Route path="/TestPage" element={<TestPage />} />*/}
     </Routes>

   </Router>
  );
}

export default App;
