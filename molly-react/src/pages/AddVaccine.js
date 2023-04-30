import React, { useEffect, useState } from 'react';
import styles from '../css/AddVaccine.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import {Button} from '../components/Button';

const AddVaccine = (props) => {
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

  let now = new Date();
  let nowMonth = now.getMonth()+1 < 10 ? `0${now.getMonth()+1}` : `${now.getMonth()+1}`;
  let nowDate = now.getDate() < 10 ? `0${now.getDate()}` : `${now.getDate()}`;

  const [form, setForm] = useState({
    year: `${now.getFullYear()}`,
    month : nowMonth,
    date: nowDate
  })

  const [viewYear, setViewYear] = useState(false);
  const [viewMonth, setViewMonth] = useState(false);
  const [viewDate, setViewDate] = useState(false);

  return (
    <div className={styles.container}>
      <div className={styles.modalContainer}>
        <div>
          <h1>💉 접종 기록 추가</h1>
        </div>
        <div>
          <ul onClick={() => {setViewYear(!viewYear)}}>
            <div className={styles.year}>
              <div className={styles.defaultyear}>{form.year}</div>
              {viewYear ? 
                <div className={styles.dropdownBtn}>
                  <MdExpandLess size="25px" color="white"/>
                </div> : 
                <div className={styles.dropdownBtn}>
                  <MdExpandMore size="25px" color="white"/>
                </div>}
              {viewYear && <YearDropdown form={form} setValue={setForm}/>}
            </div>
          </ul>
          <ul onClick={() => {setViewMonth(!viewMonth)}}>
            <div className={styles.month}>
              <div className={styles.defaultmonth}>{form.month}</div>
              {viewMonth ? 
                <div className={styles.dropdownBtn}>
                  <MdExpandLess size="25px" color="white"/>
                </div> : 
                <div className={styles.dropdownBtn}>
                  <MdExpandMore size="25px" color="white"/>
                </div>}
              {viewMonth && <MonthDropdown form={form} setValue={setForm}/>}
            </div>
          </ul>
          <ul onClick={() => {setViewDate(!viewDate)}}>
            <div className={styles.date}>
              <div className={styles.defaultdate}>{form.date}</div>
              {viewDate ? 
                <div className={styles.dropdownBtn}>
                  <MdExpandLess size="25px" color="white"/>
                </div> : 
                <div className={styles.dropdownBtn}>
                  <MdExpandMore size="25px" color="white"/>
                </div>}
              {viewDate && <DateDropdown form={form} setValue={setForm}/>}
            </div>
          </ul>
        </div>
        <div className={styles.btn}>
          <Button name="취소" onClick={props.onClick}/>
          <Button name="등록"/>
        </div>
      </div>
    </div>
  );
};

const YearDropdown = (props) => {
  let now = new Date();

  return (
    <div className={styles.yearDropdown}>
      <li onClick={() => {props.setValue({ ...props.form, year: `${now.getFullYear()}`})}}>{now.getFullYear()}</li>
      <li onClick={() => {props.setValue({ ...props.form, year: `${now.getFullYear()-1}`})}}>{now.getFullYear()-1}</li>
      <li onClick={() => {props.setValue({ ...props.form, year: `${now.getFullYear()-2}`})}}>{now.getFullYear()-2}</li>
    </div>
  )
}

const MonthDropdown = (props) => {
  return (
    <div className={styles.monthDropdown}>
      {Array(12).fill(0).map((data, index) => {
        if(index < 9) {
          return <li key={index} onClick={() => {props.setValue({ ...props.form, month: `0${index+1}`})}}>0{index+1}</li>
        }
        else {
          return <li key={index} onClick={() => {props.setValue({ ...props.form, month: `${index+1}`})}}>{index+1}</li>
        }
      })}
    </div>
  )
}

const DateDropdown = (props) => {
  let days = [];

  let month = parseInt(props.form.month).toString();
  let date = new Date(props.form.year, month, 0).getDate();
  
  for(let d = 1; d <= date; d ++) {
    if (d<10) {
      days.push("0"+d.toString());
    } else {
      days.push(d.toString());
    }
  }

  return (
    <div className={styles.dateDropdown}>
      {days.map((data, index) => {
        return <li key={index} onClick={() => {props.setValue({ ...props.form, date: data})}}>{data}</li>
      })}
    </div>
  )
}

export default AddVaccine;