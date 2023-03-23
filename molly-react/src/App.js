import { Route, Routes } from 'react-router-dom';
import './App.css';
import Home from './pages/Home';
import Calendar from './pages/Calendar';
import List from './pages/community/List';
import Hospital from './pages/Hospital';
import About from './pages/About';
import Detail from './pages/community/Detail';
import Write from './pages/community/Write';
import RegisterPet from './pages/pet/RegisterPet';
import LogIn from './pages/LogIn';

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<LogIn />}/>
        <Route path="/home" element={<Home />}/>
        <Route path="/login" element={<LogIn />}/>
        <Route path="/calendar" element={<Calendar />}/>
        <Route path="/list" element={<List />}/>
        <Route path="/hospital" element={<Hospital />}/>
        <Route path="/about" element={<About />}/>
        <Route path="/list/:id" element={<Detail />}/>
        <Route path="/list/write" element={<Write />}/>
        <Route path="/registerpet" element={<RegisterPet />}/>
      </Routes>
    </div>
  );
}

export default App;
