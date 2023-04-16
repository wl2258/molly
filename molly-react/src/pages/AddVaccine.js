import React, { useEffect } from 'react';
import styles from '../css/AddVaccine.module.css';

const AddVaccine = () => {
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
      <div className={styles.modalContainer}>
        <div>
          <h1>접종 기록 추가</h1>
        </div>
      </div>
    </div>
  );
};

export default AddVaccine;