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
  const [disabled, setDisabled] = useState(false);
  const imgRef = useRef();
  const location = useLocation();
  const params = new URLSearchParams(location.search);

  const accountId = params.get('accountId');
  const accessToken = params.get('accessToken');

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

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("nickname", nickname);
    formData.append("accountProfileImage", imgRef.current.files[0]);

    fetch(`http://localhost:8080/api/account/${accountId}`, {
      method: "POST",
      headers: {
        "Content-Type" : "multipart/form-data",
        Authorization: accessToken
      },
      body: formData,
    })
    .then(res => res.json())
    .then(res => {
      if(res.code === -1 && res.data !== null) {
        alert(res.data.nickname);
      } else {
        alert(res.msg);
      }
    })  
  }

  const checkDuplicate = (e) => {
    fetch(`http://localhost:8080/api/account/duplicate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: accessToken
      },
      body: JSON.stringify({
        nickname : nickname
      })
    })
    .then(res => res.json())
    .then(res => {
      if(res.code === 1) {
        alert(res.msg);
        setDisabled(true);
      } else {
        alert(res.msg);
        setDisabled(false);
      }
    })  
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
            <input 
              name="nickname"
              type="text" 
              value={nickname} 
              onChange={handleChange} 
              placeholder="닉네임"
              required
            />
            <span onClick={checkDuplicate}>중복확인</span>
          </div>
          {disabled ? 
            <span><Button disabled={disabled} name="저장"/></span> 
            : <span><Button disabled={disabled} name="저장" bgcolor="#D6CCC3"/></span>}
        </form>
      </div>
    </div>
  );
};

export default SignUp;