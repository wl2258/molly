import React, { useState } from 'react';
import Header from '../../components/Header';
import styles from '../../css/RegisterPet.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { FiPlus, FiCheck } from 'react-icons/fi';
import 'react-datepicker/dist/react-datepicker.css';
import {Button} from '../../components/Button.js';
import Vaccine from '../../components/pet/Vaccine';
import CustomDatePicker from '../../components/CustomDatePicker';

const RegisterPet = () => {
  const [pet, setPet] = useState(false);
  const [petValue, setPetValue] = useState('비숑');
  const [gender, setGender] = useState([]);
  const [neutered, setNeutered] = useState([]);
  const [surgery, setSurgery] = useState([]);
  const [modal, setModal] = useState(false);

  const handleGenderButton = (e) => {
    setGender(e.target.value);
  }

  const handleNeuteredButton = (e) => {
    setNeutered(e.target.value);
  }

  const handleSurgeryButton = (e) => {
    setSurgery(e.target.value);
  }

  const handleModal = () => {
    setModal(!modal);
  }

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.board}>
        <h1>반려동물 수정</h1>
        <input className={styles.name} placeholder="이름" type="text"></input>
        <br />
        <h4>품종</h4>
        <div onClick={() => {setPet(!pet)}} className={styles.sort}>
          <span className={styles.default}>{petValue}</span>
          {pet ? <span style={{position:"absolute", top:"2px", right:"10px"}}>
            <MdExpandLess size="25px" color="#AFA79F"/></span> : 
            <span style={{position:"absolute", top: "2px", right:"10px"}}>
              <MdExpandMore size="25px" color="#AFA79F"/>
            </span>
          }
          {pet && <PetDropdown setValue={setPetValue}/>}
        </div>
        <div className={styles.info}>
          <h4>생일</h4>
          <CustomDatePicker />
          <h4 style={{marginLeft: "80px"}}>몸무게</h4>
          <input className={styles.weight} type="text"></input>
          <span style={{
            color: "#827870",
            fontSize: "14px",
            marginLeft: "-25px"
          }}>kg</span>
          <br />
          <h4>성별</h4>
          <label className={styles.radio}>
            <input type="radio" onChange={handleGenderButton} value="암컷" checked={gender === "암컷"}/>
            <span>암컷</span>
            <input type="radio" onChange={handleGenderButton} value="수컷" checked={gender === "수컷"}/>
            <span>수컷</span>
          </label>
          <br />
          <h4>중성화</h4>
          <label className={styles.radio}>
            <input type="radio" onChange={handleNeuteredButton} value="함" checked={neutered === "함"}/>
            <span>함</span>
            <input type="radio" onChange={handleNeuteredButton} value="안 함" checked={neutered=== "안 함"}/>
            <span>안 함</span>
          </label>
          <br />
          <h4>수술이력</h4>
          <label className={styles.radio}>
            <input type="radio" onChange={handleSurgeryButton} value="있음" checked={surgery === "있음"}/>
            <span>있음</span>
            <input type="radio" onChange={handleSurgeryButton} value="없음" checked={surgery === "없음"}/>
            <span>없음</span>
          </label>
          {surgery === "있음" ? 
            <div className={styles.surgery}>
              <CustomDatePicker /> 
              <input className={styles.surgeryname} placeholder="수술명" type="text"></input>
              <span><FiPlus color="#AFA79F" size="18px"/></span>
            </div> : null}
          <br />
          <h4>복용약</h4>
          <div className={styles.drug}>
            <input placeholder="복용약명" type="text"></input>
            <p>start</p>
            <CustomDatePicker />
            <p>end</p>
            <CustomDatePicker />
            <span><FiPlus color="#AFA79F" size="18px"/></span>
          </div>
          <br />
          <h4>주의할 점</h4>
          <div className={styles.caution}>
            <input type="text"></input>
            <span><FiCheck color="#AFA79F" size="18px"/></span>
          </div>
          <br />
          <h4>예방접종 이력</h4>
          <button onClick={handleModal}>수정</button>
        </div>
        <div style={{marginLeft: "500px", marginBottom: "10px"}}>
          <Button name="등록"/>
        </div>
        {modal && <Vaccine onClick={handleModal}/>}
      </div>   
    </div>
  );
};

const PetDropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('비숑')}}>비숑</li>
      <li onClick={() => {props.setValue('말티즈')}}>말티즈</li>
      <li onClick={() => {props.setValue('푸들')}}>푸들</li>
      <li onClick={() => {props.setValue('웰시코기')}}>웰시코기</li>
    </div>
  )
}

export default RegisterPet;