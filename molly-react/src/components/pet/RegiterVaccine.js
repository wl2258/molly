import React, { forwardRef, useEffect, useRef, useState } from 'react';
import styles from '../../css/Vaccine.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { FiPlus } from 'react-icons/fi';
import { TiDelete, TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';

const RegisterVaccine = (props) => {
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
  const vaccineNo = useRef(1);

  const [vaccineDate, setVaccineDate] = useState(new Date());

  return (
    <div className={styles.vaccine}>
      <div className={styles.modalContainer}>
        <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
        <h1>예방접종 이력 추가</h1>
        <ul>
          <div onClick={() => { setVaccine(!vaccine) }} className={styles.sort}>
            <span className={styles.default}>{vaccineValue}</span>
            {vaccine ?
              <span style={{ position: "absolute", right: "8px" }}><MdExpandLess size="25px" color="#AFA79F" /></span> :
              <span style={{ position: "absolute", right: "8px" }}><MdExpandMore size="25px" color="#AFA79F" /></span>}
            {vaccine && <VaccineDropdown setValue={setVaccineValue} petType={props.petType} />}
          </div>
        </ul>
        <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
          <DatePicker
            locale={ko}
            selected={vaccineDate}
            onChange={(date) => setVaccineDate(date)}
            dateFormat="yyyy/MM/dd"
            customInput={<CustomInput />}
          />
        </div>
        <span onClick={() => {
          props.setVaccineHistory([...props.vaccineHistory, {
            vaccineId: vaccineNo.current++,
            vaccinationName: vaccineValue,
            vaccinationDate: props.dateFormat(vaccineDate),
          }])
        }}><FiPlus color="#AFA79F" size="18px" /></span>
        <div>
          {props.vaccineHistory.map((data, index) => {
            return (
              <div key={index} className={styles.vaccineHistory}>
                <span>{data.vaccinationName}</span>
                <span>{data.vaccinationDate}</span>
                <span onClick={() => {
                  props.setVaccineHistory(props.vaccineHistory.filter(vaccineHistory => vaccineHistory.vaccineId !== data.vaccineId))
                }}><TiDelete size="18px" color="#827870" /></span>
              </div>
            )
          })}
        </div>
      </div>
    </div>
  );
};

const VaccineDropdown = (props) => {
  return (
    <>
      {props.petType === "DOG" ?
        <div className={styles.dropdown}>
          <li onClick={() => { props.setValue('종합백신 1차') }}>종합백신 1차</li>
          <li onClick={() => { props.setValue('종합백신 2차') }}>종합백신 2차</li>
          <li onClick={() => { props.setValue('종합백신 3차') }}>종합백신 3차</li>
          <li onClick={() => { props.setValue('종합백신 4차') }}>종합백신 4차</li>
          <li onClick={() => { props.setValue('종합백신 5차') }}>종합백신 5차</li>
          <li onClick={() => { props.setValue('컨넬코프 1차') }}>컨넬코프 1차</li>
          <li onClick={() => { props.setValue('컨넬코프 2차') }}>컨넬코프 2차</li>
          <li onClick={() => { props.setValue('코로나 장염 1차') }}>코로나 장염 1차</li>
          <li onClick={() => { props.setValue('코로나 장염 2차') }}>코로나 장염 2차</li>
          <li onClick={() => { props.setValue('인플루엔자 1차') }}>인플루엔자 1차</li>
          <li onClick={() => { props.setValue('인플루엔자 2차') }}>인플루엔자 2차</li>
          <li onClick={() => { props.setValue('광견병') }}>광견병</li>
          <li onClick={() => { props.setValue('항체가검사') }}>항체가검사</li>
          <li onClick={() => { props.setValue('심장사상충') }}>심장사상충</li>
          <li onClick={() => { props.setValue('외부기생충') }}>외부기생충</li>
        </div> : props.petType === "CAT" ?
          <div className={styles.dropdown}>
            <li onClick={() => { props.setValue('종합백신 1차') }}>종합백신 1차</li>
            <li onClick={() => { props.setValue('종합백신 2차') }}>종합백신 2차</li>
            <li onClick={() => { props.setValue('종합백신 3차') }}>종합백신 3차</li>
            <li onClick={() => { props.setValue('광견병') }}>광견병</li>
            <li onClick={() => { props.setValue('전염성 복막염') }}>전염성 복막염</li>
            <li onClick={() => { props.setValue('항체가검사') }}>항체가검사</li>
            <li onClick={() => { props.setValue('심장사상충') }}>심장사상충</li>
          </div> : <div className={styles.dropdown}>
            <li onClick={() => { props.setValue('유행성 출혈병') }}>유행성 출혈병</li>
            <li onClick={() => { props.setValue('광견병') }}>광견병</li>
          </div>
      }
    </>
  )
}

const CustomInput = forwardRef(({ value, onClick }, ref) => (
  <button className={styles.custominput} onClick={onClick} ref={ref}>
    {value}
  </button>
))

export default RegisterVaccine;