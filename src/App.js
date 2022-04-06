import './App.css';
import SoloInGameInterface from './component/single/SoloInGameInterface';
import Main from './component/main/Main';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';
import SoketTest from './component/single/SoketTest';
import SoloGameCreate from './component/single/SoloGameCreate';

function App() {
  return (
   <Router>
     <Routes>
       <Route path="/" element={<Main />} />
       <Route path="/SoloInGameInterface" element={<SoloInGameInterface />} />
       <Route path="/SoloGameCreate" element={<SoloGameCreate />} />
       <Route path="/SoketTest" element={<SoketTest />} />
     </Routes>

   </Router>
  );
}

export default App;
