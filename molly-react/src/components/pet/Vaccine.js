import React, { useEffect, useState } from 'react';
import styles from '../../css/Vaccine.module.css';
import { Button } from '../Button';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import CustomDatePicker from '../../components/CustomDatePicker';

const Vaccine = (props) => {
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
  const [vaccine, setVaccine] = useState(false);
  const [vaccineValue, setVaccineValue] = useState('종합백신 1차');

  return (
    <div className={styles.vaccine}>
      <div className={styles.modalContainer}>
        <h1>예방접종 이력 추가</h1>
        <div>
          <Button onClick={props.onClick} name="취소"/>
          <Button name="저장"/>
        </div>
        <ul onClick={() => {setVaccine(!vaccine)}}>
          <div className={styles.sort}>
            <span className={styles.default}>{vaccineValue}</span>
            {vaccine ? 
              <span style={{position:"absolute", right: "8px"}}><MdExpandLess size="25px" color="#AFA79F"/></span> : 
              <span style={{position:"absolute", right: "8px"}}><MdExpandMore size="25px" color="#AFA79F"/></span>}
            {vaccine && <VaccineDropdown setValue={setVaccineValue}/>}
          </div>
        </ul>
        <span><CustomDatePicker /></span>
      </div>
    </div>
  );
};

const VaccineDropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('종합백신 1차')}}>종합백신 1차</li>
      <li onClick={() => {props.setValue('종합백신 2차')}}>종합백신 2차</li>
      <li onClick={() => {props.setValue('컨넬코프 1차')}}>컨넬코프 1차</li>
    </div>
  )
}

export default Vaccine;