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
  const [warning, setWarning] = useState(false);

  const imgRef = useRef();
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  let color = disabled ? "#D6CCC3" : "#B27910";

  const accountId = params.get('accountId');

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
    if(imgRef.current.files[0] !== undefined) {
      formData.append("accountProfileImage", imgRef.current.files[0]);
    }

    fetch(`http://localhost:8080/api/account/${accountId}`, {
      method: "POST",
      body: formData,
    })
    .then(res => {
      if(res.status === 200) {
        window.location.replace("/");
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
      setWarning(false);
    }
    else { 
      setEffective(false);
      setWarning(true);
    }
  }

  const checkDuplicate = (e) => {
    fetch(`http://localhost:8080/api/account/duplicate`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        nickname : nickname
      })
    })
    .then(res => res.json())
    .then(res => {
      if(res.code === 1) {
        effective === true ? setDisabled(false) : setDisabled(true);
        setWarning(false);
        setDuplicate(2);
      } else {
        setDisabled(true);
        setWarning(false);
        setDuplicate(1);
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
              onBlur={checkNickname}
            />
            <span onClick={checkDuplicate}>중복확인</span>
          </div>
          {
            warning === false ? null :
            <span style={{left: "173px"}} className={styles.duplicatepass}>한글 또는 영어를 사용하여<br/>10자 이내로 작성해주세요.</span>}
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