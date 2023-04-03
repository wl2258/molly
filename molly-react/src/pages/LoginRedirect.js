import axios from 'axios';
import React, { useEffect } from 'react';

const LoginRedirect = () => {
  useEffect(() => {
    let code = new URL(window.location.href).searchParams.get('code');
    const Login = async () => {
      await axios
        .get(`http://localhost:8080/login/oauth2/kakao?code=${code}`)
        .then((res) => {
          localStorage.clear();
          localStorage.setItem("token", res.headers.authorization);
          console.log(res.headers.accountId);
        })
    }
    Login();
  }, []);
  
  return (
    <div>
    </div>
  );
};

export default LoginRedirect;