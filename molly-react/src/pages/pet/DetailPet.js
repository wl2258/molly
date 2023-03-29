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
          surgeryname: '수직이도성형술',
          surgerydate: '2023/02/18'
        },
        {
          surgeryname: '유선종양제거',
          surgerydate: '2023/03/05'
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

  return (
    <div>
      <Header />
      <CustomBody>
        <img
          className={styles.profileimg}
          src={process.env.PUBLIC_URL + '/img/profile.png'}
          alt="프로필 이미지"
          width="70px"
        />
        <h1>{detail[0].name}</h1>
      </CustomBody>
    </div>
  );
};

export default DetailPet;