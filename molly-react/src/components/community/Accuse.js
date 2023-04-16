import React, { useEffect } from 'react';
import styles from '../../css/Accuse.module.css';
import { Button } from '../Button';

const Accuse = (props) => {
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

  return (
    <div className={styles.container}>
      <div className={styles.modal}>
        <h1>🚨 신고하기</h1>
        <div>
          <input placeholder="제목"></input> 
          <textarea placeholder="내용을 입력하세요."></textarea>
        </div>
        <div>
          <Button name="취소" onClick={props.onClick}/>
          <Button name="신고하기"/>
        </div>
      </div>
    </div>
  );
};

export default Accuse;