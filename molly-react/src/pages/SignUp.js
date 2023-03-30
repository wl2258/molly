import React, { useEffect, useRef, useState } from 'react';
import styles from '../css/SignUp.module.css';
import {Button} from '../components/Button';

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
        <form>
          <div className={styles.profileuser}>
            <img
              className={styles.profileimg}
              src={imgFile ? imgFile : process.env.PUBLIC_URL + '/img/profile.png'}
              alt="프로필 이미지"
            />
          </div>
          <label className={styles.profilelabel} htmlFor="profileImg">프로필 이미지 추가</label>
          <input
            className={styles.profileinput}
            type="file"
            accept="image/*"
            id="profileImg"
            onChange={saveImgFile}
            ref={imgRef}
          />
        </form>
        <div className={styles.modal}>
          <input placeholder="닉네임"></input>
          <span><Button name="저장"/></span>
        </div>
      </div>
    </div>
  );
};

export default SignUp;