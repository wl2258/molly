import React from 'react';
import styles from '../css/LoginTest.module.css';

const LoginTest = () => {
  const KAKAO_AUTH_URL = `http://localhost:8080/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `http://localhost:8080/oauth2/authorization/google`;
  
  
  const kakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  }

  const googleLogin = () => {
    window.location.href = GOOGLE_AUTH_URL;
  }

  return (
    <div className={styles.container}>
      <div className={styles.login}>
        <div>
          <img src={process.env.PUBLIC_URL + '/molly-logo-title.png'} alt="molly-logo" width="120px"/>
        </div>
        <p>반려동물 예방접종 일정관리 사이트</p>
        <p>회원가입 👋</p>
        <div onClick={kakaoLogin}>
          <img src='img/kakao_login_medium_wide.png' alt="kakao"/>
        </div>
        <div style={{marginTop: "10px"}} onClick={googleLogin}>
          <img src='img/google_login.png' alt="kakao" width="81%"/>
        </div>
      </div>
    </div>
  );
};

export default LoginTest;