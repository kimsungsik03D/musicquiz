import './App.css';
import SoloInGameInterface from './pages/single/SoloInGameInterface';
import Main from './pages/main/Main';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';
import SoketTest from './pages/single/SoketTest';
import SoloGameCreate from './pages/single/SoloGameCreate';

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
