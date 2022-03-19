import './App.css';
import SoloInGameInterface from './component/single/SoloInGameInterface';
import Main from './component/main/Main';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';

function App() {
  return (
   <Router>
     <Routes>
       <Route path="/" element={<Main />} />
       <Route path="/SoloInGameInterface" element={<SoloInGameInterface />} />
     </Routes>

   </Router>
  );
}

export default App;
