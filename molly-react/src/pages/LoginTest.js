import React from 'react';
import { REST_API_KEY, REDIRECT_URI } from './KakaoLoginData';
import styles from '../css/LoginTest.module.css';

const LoginTest = () => {
  const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;
  
  const handleLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  }

  return (
    <div className={styles.container}>
      <div className={styles.login}>
        <div>
          <img src={process.env.PUBLIC_URL + '/molly-logo-title.png'} alt="molly-logo" width="120px"/>
        </div>
        <p>반려동물 예방접종 일정관리 사이트</p>
        <p>회원가입 👋</p>
        <div className={styles.kakao} onClick={handleLogin}>
          <img src='img/kakao_login_medium_wide.png' alt="kakao"/>
        </div>
      </div>
    </div>
  );
};

export default LoginTest;