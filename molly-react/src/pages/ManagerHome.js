import React from 'react';
import styles from '../css/ManagerHome.module.css';
import styled from 'styled-components';
import { NavLink } from 'react-router-dom';


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

let CustomBody = styled.div`
  margin-top: 140px;
  padding: 0 10%;
  display: flex;
`

const ManagerHome = () => {
  return (
    <div>
      <header className={styles.header}>
        <div>
          <div className={styles.logo}>
            <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="130px"/>
          </div>
          <div className={styles.navcontainer}>
            <nav className={styles.navigation}>
              <div>
                <CustomNavLink 
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/manager/home">
                    Home
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")} 
                  to="/list">
                    Community
                </CustomNavLink>
              </div>
            </nav>
          </div>
          <div className={styles.logout}>
            <span onClick={() => {console.log("click")}}>로그아웃</span>
          </div>
        </div>
      </header>
      <CustomBody>
        <div className={styles.accuse}>
          <h1>🚨 신고목록</h1>
          <div className={styles.accuseBoard}>
            <div>
              <h3>민트초코가 극혐이라고 합니다. 탈퇴시켜주세요.</h3>
              <span>민초단</span>
            </div>
            <div>
              <h3>치킨은 양념인데 간장이라고 우기네요.</h3>
              <span>양반후반</span>
            </div>
          </div>
        </div>
        <div className={styles.member}>
          <div>
            <h1>👤 회원목록</h1>
            <input className={styles.search} placeholder="회원 검색"></input>
          </div>
          <div className={styles.memberlist}>
            <div>
              <h3>일당백</h3>
              <span>탈퇴</span>
            </div>
            <div>
              <h3>민초단</h3>
              <span>탈퇴</span>
            </div>
          </div>
        </div>
      </CustomBody>
    </div>
  );
};

export default ManagerHome;