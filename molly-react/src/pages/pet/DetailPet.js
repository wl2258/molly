import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import styles from '../../css/DetailPet.module.css';
import Header from '../../components/Header';
import styled from 'styled-components';

let CustomBody = styled.body`
  margin-top: 140px;
  padding: 0 14%;
`;

const DetailPet = () => {
  let {id} = useParams();
  const [text] = useState([
    {
      id: 1,
      name: '까까',
      species: '비숑',
      birthday: '2023/01/13',
      weight: 5,
      gender: 'female',
      neutered: false,
      surgery: [
        {
          "surgeryName": "수직이도성형술",
					"surgeryDate": "2023-01-01"
        },
        {
          "surgeryName": "수술",
					"surgeryDate": "2023-03-06"
        }
      ],
      medication: [
				{
					medicationName: "넥스가드",
					medicationStart: "2023/02/01",
					medicationEnd: "2023/02/15"
				},
        {
					medicationName: "진드기약",
					medicationStart: "2023/02/01",
					medicationEnd: null
				}
      ],
      caution: "귀여움",
      vaccine: [
        {
          vaccineName: "심장사상충",
          vaccineDate: "2023/02/15"
        },
        {
          vaccineName: "종합백신 2차",
          vaccineDate: "2023/03/17"
        }
      ]
    }
  ])

  let detail = text.filter((item) => item.id === parseInt(id));
  let gender = detail[0].gender === 'female' ? true : false;
  let neutered = detail[0].neutered === true ? true : false;
  let surgery = detail[0].surgery[0] === undefined ? false : true;

  return (
    <div>
      <Header />
      <CustomBody>
        <div className={styles.container}>
          <img
            className={styles.profileimg}
            src={process.env.PUBLIC_URL + '/img/profile.png'}
            alt="프로필 이미지"
            width="70px"
          />
          <h1>{detail[0].name}</h1>
          <br/>
          <h4>품종</h4>
          <span>{detail[0].species}</span>
          <br/>
          <h4>생일</h4>
          <span>{detail[0].birthday}</span>
          <h4>몸무게</h4>
          <span>{detail[0].weight}kg</span>
          <br/>
          <h4>성별</h4>
          <label className={styles.radio}>
            <input type="radio" readOnly={true} value="암컷" checked={gender}/>
            <span>암컷</span>
            <input type="radio" readOnly={true} value="수컷" checked={!gender}/>
            <span>수컷</span>
          </label>
          <br/>
          <h4>중성화</h4>
          <label className={styles.radio}>
            <input type="radio" readOnly={true} value="함" checked={neutered}/>
            <span>함</span>
            <input type="radio" readOnly={true} value="안 함" checked={!neutered}/>
            <span>안 함</span>
          </label>
          <br/>
          <h4>수술이력</h4>
          <label className={styles.radio}>
            <input type="radio" readOnly={true} value="있음" checked={surgery}/>
            <span>있음</span>
            <input type="radio" readOnly={true} value="없음" checked={!surgery}/>
            <span>없음</span>
          </label>
          <br/>
          {surgery ? 
            detail[0].surgery.map((item) => {
              return (
                <div>
                  <span>{item.surgeryDate}</span>
                  <span>{item.surgeryName}</span>
                  <br/>
                </div>
              )
            })
            : null}
          <h4>복용약</h4>
          {
            detail[0].medication.map((item) => {
              return (
                <div>
                  <p>{item.medicationName}</p>
                  <span>{item.medicationStart}</span>
                  <span>{item.medicationEnd}</span>
                </div>
              )
            })
          }
          <br/>
          <h4>주의할 점</h4>
        </div>
      </CustomBody>
    </div>
  );
};

export default DetailPet;