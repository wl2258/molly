import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/SignUp.module.css';
import {Button} from '../components/Button';
import { useLocation } from 'react-router-dom';

const SignUp = () => {
  useEffect(() => {
    document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = '';
      window.scrollTo(0, parseInt(scrollY || '0', 10) * -1);
    };
  }, []);

  const [imgFile, setImgFile] = useState("");
  const [nickname, setNickName] = useState("");
  const [disabled, setDisabled] = useState(true);
  const [duplicate, setDuplicate] = useState(0);
  const [effective, setEffective] = useState(false);
  const [effectiveColor, setEffectiveColor] = useState("");

  const imgRef = useRef();
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  let color = disabled ? "#D6CCC3" : "#B27910";

  const accessToken = params.get('accessToken');
  const refreshToken = params.get('refreshToken');

  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);

  const handleChange = (e) => {
    setNickName(e.target.value);
  }

  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
        setImgFile(reader.result);
    };
  };

  const issuedToken = () => {
    fetch(`http://localhost:8080/api/token/refresh`, {
      method: "POST",
      headers: {
        "Refresh-Token" : refreshToken
      }
    })
    .then(res => {
      if(res.status === 200) {
        console.log("토큰 재발급 성공");
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        let accessToken = res.headers.get("Authorization");
        let refreshToken = res.headers.get("Refresh-token");
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);
      }
      else if(res.status === 401) {
        console.log("토큰 재발급 실패");
        window.location.replace("/login");
      }
    })
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("nickname", nickname);
    if(imgRef.current.files[0] !== undefined) {
      formData.append("accountProfileImage", imgRef.current.files[0]);
    }

    fetch(`http://localhost:8080/api/auth/account/save`, {
      method: "POST",
      headers: {
        Authorization : accessToken
      },
      body: formData,
    })
    .then(res => {
      if(res.status === 200) {
        window.location.replace("/");
      }
      else if(res.status === 400) {
        issuedToken();
        handleSubmit();
      }
      else if(res.status === 401) {
        console.log("인증 실패");
        window.location.replace("/login");
      }
      else if(res.status === 403) {
        alert("권한이 없습니다.");
      }
      else res.json()
    })
    .then(res => {
      if(res.code === -1 && res.data !== null) {
        console.log(res.data.nickname);
      } else if(res.code === -1 && res.data === null) {
        console.log(res.msg);
      }
    })  
  }

  const checkNickname = (e) => {
    const regExp = /^[가-힣a-zA-Z]{1,10}$/;
    if(regExp.test(e.target.value) === true) {
      setEffective(true);
    }
    else { 
      setEffective(false);
    }
  }

  const checkDuplicate = (e) => {
    if(effective === false) {
      setEffectiveColor("red");
    } else {
      setEffectiveColor("#827870");
      fetch(`http://localhost:8080/api/auth/account/duplicate`, {
        method: "POST",
        headers: {
          Authorization : accessToken,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          nickname : nickname
        })
      })
      .then(res => {
        if(res.status === 200) {
          return res.json()
        }
        else if(res.status === 400) {
          issuedToken();
          checkDuplicate();
        }
        else if(res.status === 401) {
          console.log("인증 실패");
          window.location.replace("/login");
        }
        else if(res.status === 403) {
          alert("권한이 없습니다.");
        }
      })
      .then(res => {
        if(res.code === 1) {
          setDisabled(false);
          setDuplicate(2);
        } else {
          setDisabled(true);
          setDuplicate(1);
        }
      }) 
    }
  }
  
  return (
    <div className={styles.container}>
      <div className={styles.modalContainer}>
        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <label htmlFor="profileImg">
            <div className={styles.profileuser}>
              <img
                className={styles.profileimg}
                src={imgFile ? imgFile : process.env.PUBLIC_URL + '/img/profile.png'}
                alt="프로필 이미지"
              />
            </div>
          </label>
          <label className={styles.profilelabel} htmlFor="profileImg">프로필 이미지 추가</label>
          <input
            name="accountProfileImage"
            className={styles.profileinput}
            type="file"
            accept="image/*"
            id="profileImg"
            onChange={saveImgFile}
            ref={imgRef}
          />
          <div className={styles.modal}>
            <span style={{color: `${effectiveColor}`}} className={styles.nicknameguide}>
              한글/영어를 사용하여 10자 이내로 작성
            </span>
            <input 
              name="nickname"
              type="text" 
              value={nickname} 
              onChange={handleChange} 
              placeholder="닉네임"
              required
              onBlur={checkNickname}
              maxLength="10"
            />
            <span onClick={checkDuplicate}>중복확인</span>
          </div>
          {duplicate === 0 ? null : 
            duplicate === 1 ? <span style={{color:"red"}} className={styles.duplicatepass}>사용 불가능한 닉네임입니다.</span> 
              : <span className={styles.duplicatepass}>사용 가능한 닉네임입니다.</span>}
          <span><Button disabled={disabled} name="저장" bgcolor={color}/></span>
        </form>
      </div>
    </div>
  );
};

export default SignUp;