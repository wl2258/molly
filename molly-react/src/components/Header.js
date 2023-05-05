import React, { useEffect, useRef, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import styles from '../css/Header.module.css'
import styled from 'styled-components';
import {RiAccountCircleLine} from 'react-icons/ri';
import {TbDog, TbBell} from 'react-icons/tb';

let CustomNavLink = styled(NavLink)`
  color: #AFA79F;
  &:link {
    text-decoration: none;
  }
  &.active {
    color: #827870;
    font-weight: 900;
  }
`;

const Header = () => {
  const userDropdownRef = useRef(null);
  const petDropdownRef = useRef(null);
  const alarmRef = useRef(null);
  const [userView, setUserView] = useState(false);
  const [petView, setPetView] = useState(false);
  const [alarmView, setAlarmView] = useState(false);
  const [scrollPosition, setScrollPosition] = useState(0);
  const navigate = useNavigate();

  const updateScroll = () => {
    setScrollPosition(window.scrollY || document.documentElement.scrollTop);
  }
  useEffect(()=>{
      window.addEventListener('scroll', updateScroll);
  });

  useEffect(() => {
    const handleOutsideClose = (e) => {
      if(userView && (!userDropdownRef.current.contains(e.target))) setUserView(false);
    };
    document.addEventListener('click', handleOutsideClose);
    
    return () => document.removeEventListener('click', handleOutsideClose);
  }, [userView]);
  
  useEffect(() => {
    const handleOutsideClose = (e) => {
      if(petView && (!petDropdownRef.current.contains(e.target))) setPetView(false);
    };
    document.addEventListener('click', handleOutsideClose);
    
    return () => document.removeEventListener('click', handleOutsideClose);
  }, [petView]);

  useEffect(() => {
    const handleOutsideClose = (e) => {
      if(alarmView && (!alarmRef.current.contains(e.target))) setAlarmView(false);
    };
    document.addEventListener('click', handleOutsideClose);
    
    return () => document.removeEventListener('click', handleOutsideClose);
  }, [alarmView]);

  return (
    <div style={{zIndex:"4", position: "fixed"}}>
      {scrollPosition < 100 ? 
        <header className={styles.header}>
          <div>
            <div className={styles.weather}>
              
            </div>
            <div className={styles.logo} onClick={() => navigate('/')}>
              <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="160px"/>
            </div>
            <div className={styles.icon}>
              <div ref={userDropdownRef} onClick={() => {setUserView(!userView)}}>
                <span><RiAccountCircleLine color="#AFA79F" size="29px"/></span>
                { userView && <div className={styles.userinfo}>
                  <UserDropdown/></div> }
              </div>
              <div ref={petDropdownRef} onClick={() => {setPetView(!petView)}}>
                <span><TbDog color="#AFA79F" size="29px"/></span>
                { petView && <div className={styles.petinfo}>
                  <PetDropdown/></div> }
              </div>
              <div ref={alarmRef} onClick={() => {setAlarmView(!alarmView)}}>
                <span><TbBell color="#AFA79F" size="29px"/></span>
                { alarmView && <div className={styles.alarm}>
                  <AlarmDropdown/></div> }
              </div>
            </div>
          </div>
          <div className={styles.navcontainer}>
            <nav className={styles.navigation}>
              <div>
                <CustomNavLink 
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/home">
                    Home
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink 
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/calendar">
                    Calendar
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")} 
                  to="/list">
                    Community
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink 
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/hospital">
                    Hospital
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")} 
                  to="/about">
                    About
                </CustomNavLink>
              </div>
            </nav>
          </div>
        </header> :
        <header className={styles.changeheader}>
          <div>
            <div className={styles.changelogo} onClick={() => navigate('/')}>
              <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="130px"/>
            </div>
            <div className={styles.changenavcontainer}>
              <nav className={styles.changenavigation}>
                <div>
                  <CustomNavLink 
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/home">
                      Home
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink 
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/calendar">
                      Calendar
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")} 
                    to="/list">
                      Community
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink 
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/hospital">
                      Hospital
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")} 
                    to="/about">
                      About
                  </CustomNavLink>
                </div>
              </nav>
            </div>
          </div>
        </header>}
    </div>
  );
};

const UserDropdown = () => {
  let navigate = useNavigate();
  
  return (
    <div className={styles.userdropdown}>
      <li onClick={() => {navigate('/userinfo')}}>사용자 정보</li>
      <li onClick={() => {}}>로그아웃</li>
    </div>
  )
}

const PetDropdown = () => {
  let navigate = useNavigate();
  const [pet] = useState(['molly']);

  return (
    <div className={styles.petdropdown}>
      {pet.map((item, index) => {
        return (
          item !== '' && 
            <li onClick={() => {navigate(`/detailpet/${item}`)}}>
              <img className={styles.petimg} src={process.env.PUBLIC_URL + '/img/DOG-logo.png'} alt="puppy" width="36px"/>{item}
            </li>
        )
      })}
      {pet.length === 0 ? <li style={{borderRadius: "10px"}} onClick={() => {navigate('/registerpet')}}>추가하기</li>:
        <li onClick={() => {navigate('/registerpet')}}>추가하기</li>}
    </div>
  )
}

const AlarmDropdown = () => {
  return (
    <div className={styles.alarmdropdown}>
      <li>
        <p>💉접종알림</p>
        <p>까까의 종합백신 2차 접종이 일주일 남았습니다.</p>
      </li>
      <li>
        <p>💉접종알림</p>
        <p>마루의 컨넬코프 2차 접종이 일주일 남았습니다.</p>
      </li>
    </div>
  )
}

export default Header;