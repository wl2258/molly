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
import First from './pages/First';
import DetailPet from './pages/pet/DetailPet';
import LoginRedirect from './pages/LoginRedirect';

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<First />}/>
        <Route path="/login" element={<LogIn />}/>
        <Route path="/login/oauth2/code/kakao" element={<LoginRedirect />}/>
        <Route path="/home" element={<Home />}/>
        <Route path="/home/:id" element={<Home />}/>
        <Route path="/login" element={<LogIn />}/>
        <Route path="/calendar" element={<Calendar />}/>
        <Route path="/list" element={<List />}/>
        <Route path="/hospital" element={<Hospital />}/>
        <Route path="/about" element={<About />}/>
        <Route path="/list/:id" element={<Detail />}/>
        <Route path="/list/write" element={<Write />}/>
        <Route path="/registerpet" element={<RegisterPet />}/>
        <Route path="/detailpet/:id" element={<DetailPet />}/>
      </Routes>
    </div>
  );
}

export default App;
