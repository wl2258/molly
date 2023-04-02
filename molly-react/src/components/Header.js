import React, { useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import styles from '../css/Header.module.css'
import styled from 'styled-components';

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
  const [userView, setUserView] = useState(false);
  const [petView, setPetView] = useState(false);
  const [alarmView, setAalarmView] = useState(false);
  const navigate = useNavigate();

  return (
    <div style={{zIndex:"3"}}>
      <header className={styles.header}>
        <div className={styles.empty}/>
        <div className={styles.logo} onClick={() => navigate('/')}>
          <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="120px"/>
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
        <div className={styles.iconempty}/>
        <div className={styles.icon}>
          <div onClick={() => {setUserView(!userView)}}>👤</div>
          <div onClick={() => {setPetView(!petView)}}>🐶</div>
          <div onClick={() => {setAalarmView(!alarmView)}}>🔔</div>
        </div>
        <div className={styles.iconempty}/>
      </header>
      <div className={styles.navuser}>
        <ul>
          { userView && <div className={styles.userinfo}>
            <UserDropdown/></div> }
        </ul>
        <ul>
          { petView && <div className={styles.petinfo}>
            <PetDropdown/></div> }
        </ul>
        <ul>
          { alarmView && <div className={styles.alarm}>
            <AlarmDropdown/></div> }
        </ul>
      </div>
    </div>
  );
};

const UserDropdown = () => {
  return (
    <div className={styles.userdropdown}>
      <li onClick={() => {}}>사용자 정보</li>
      <li onClick={() => {}}>로그아웃</li>
    </div>
  )
}

const PetDropdown = () => {
  let navigate = useNavigate();
  const [pet] = useState(['까까', '보리', '마루']);

  return (
    <div className={styles.petdropdown}>
      {pet.map((item, index) => {
        return (
          item !== '' && <li onClick={() => {navigate(`/detailpet/${index+1}`)}}>{item}</li>
        )
      })}
      <li onClick={() => {navigate('/registerpet')}}>추가하기</li>
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