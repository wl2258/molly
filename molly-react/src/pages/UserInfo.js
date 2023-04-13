import React, { useRef, useState } from 'react';
import Header from '../components/Header';
import styles from '../css/UserInfo.module.css';
import {Button} from '../components/Button';
import {MdModeEdit, MdDelete} from 'react-icons/md';

const UserInfo = () => {
  const [imgFile, setImgFile] = useState("");
  const [edit, setEdit] = useState(false);
  const [nickname, setNickName] = useState("");
  const [disabled, setDisabled] = useState(true);
  const [duplicate, setDuplicate] = useState(0);
  const [effective, setEffective] = useState(false);
  const [effectiveColor, setEffectiveColor] = useState("");

  let color = disabled ? "#D6CCC3" : "#B27910";

  const imgRef = useRef();

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
    console.log(imgRef);
  };

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
    setDisabled(true);
    setDuplicate(0);
  }

  const deleteImg = () => {
    setImgFile("");
    console.log(imgRef);
  }

  return (
    <div>
      <Header />
      <div className={styles.box}>
        <div className={styles.container}>
          <div className={styles.profile}>
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
              <span onClick={deleteImg} className={styles.imgdelete} htmlFor="profileImg">
                <MdDelete color="#827870" size="17px"/>
              </span>
              <input
                name="accountProfileImage"
                className={styles.profileinput}
                type="file"
                accept="image/*"
                id="profileImg"
                onChange={saveImgFile}
                ref={imgRef}
              />
              {edit ? 
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
                  {duplicate === 0 ? null : 
                    duplicate === 1 ? <span style={{color:"red"}} className={styles.duplicatepass}>사용 불가능한 닉네임입니다.</span> 
                    : <span className={styles.duplicatepass}>사용 가능한 닉네임입니다.</span>}
                  <span className={styles.cancle}><Button onClick={() => {setEdit(false)}} name="취소"/></span>
                  <span className={styles.save}><Button disabled={disabled} bgcolor={color} name="저장"/></span>
                </div> :
                <div className={styles.modal}>
                  <div className={styles.nickname}>
                    일당백
                  </div>
                  <span className={styles.editicon} onClick={()=>{setEdit(true)}}><MdModeEdit color="#827870" size="25px"/></span>
                </div>
              }
            </form>
            <div></div>
            <div></div>
          </div>
          <div className={styles.info}>
            <div>
              <h1>회원가입 정보</h1>
              <img src={process.env.PUBLIC_URL + '/img/kakao-account.png'} alt="kakao 로고" width="40px" height="40px"/>
              <p>illdang100@naver.com</p>
            </div>
            <div>
              <h1>탈퇴하기</h1>
              <button>탈퇴하기</button>
            </div>
            <p>탈퇴 시 작성하신 포스트 및 댓글과 반려동물 등록 정보가 모두 삭제되며 복구되지 않습니다.</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserInfo;