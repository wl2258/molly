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
  const imgRef = useRef();
  const location = useLocation();
  const params = new URLSearchParams(location.search);

  const accountId = params.get('accountId');
  const accessToken = params.get('accessToken')

  localStorage.setItem('token', accessToken);

  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
        setImgFile(reader.result);
    };
  };
  
  return (
    <div className={styles.container}>
      <div className={styles.modalContainer}>
        <form encType="multipart/form-data">
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
            className={styles.profileinput}
            type="file"
            accept="image/*"
            id="profileImg"
            onChange={saveImgFile}
            ref={imgRef}
          />
          <div className={styles.modal}>
            <input placeholder="닉네임"></input>
            <span>중복확인</span>
          </div>
          <span><Button name="저장"/></span>
        </form>
        {console.log(accountId)}
      </div>
    </div>
  );
};

export default SignUp;