import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import styles from '../../css/DetailPet.module.css';
import Header from '../../components/Header';
import styled from 'styled-components';
import {Button} from '../../components/Button';
import axios from 'axios';

let CustomBody = styled.div`
  margin-top: 240px;
  margin-bottom: 50px;
`;

const DetailPet = () => {
  let {id} = useParams();
  const [text, setText] = useState({
      "userId": 32492,
      "petId": 1234,
      "peType": "DOG",
      "petName": "molly",
      "species": "MALTESE",
      "profileImage": "N23498SAJSJAFIOSJ...IJSDFJODISDJOISJS",
      "birthDate": "2013-08-07",
      "gender": "FEMALE",
      "neuteredStatus" : "false",
      "weight" : 3.4,
      "caution" : "분리불안 심함",
      "surgery": [
        {
          "surgeryName": "수직이도성형술",
          "surgeryDate": "2023-01-01"
        },
      ],
      "medication": [
        {
          "medicationName": "넥스가드",
          "medicationStart": "2023-02-01",
          "medicationEnd": "2023-02-15"
        },
      ],
      "vaccination": [
        {
          "vaccinationName": "종합백신1차",
          "vaccinationDate": "2018-01-01"
        },
      ]
    }
  )

  const axiosInstance = axios.create({
    baseURL: "http://localhost:8080",
  });

  axiosInstance.interceptors.response.use(
    (res) => {
      return res;
    },
    async (error) => {
      try {
        const errResponseStatus = error.response.status;
        const prevRequest = error.config;
        const errMsg = error.response.data.msg;

        if(errResponseStatus === 400 && errMsg === "만료된 토큰입니다") {
          const preRefreshToken = localStorage.getItem("refreshToken");
          if(preRefreshToken) {
            async function issuedToken() {
              const config = {
                headers: {
                  "Refresh-Token": preRefreshToken
                }
              }
              return await axios
                .post(`http://localhost:8080/api/token/refresh`, null, config)
                .then(async (res) => {
                  localStorage.clear();
                  const reAccessToken = res.headers.get("Authorization");
                  const reRefreshToken = res.headers.get("Refresh-token");
                  localStorage.setItem("accessToken", reAccessToken);
                  localStorage.setItem("refreshToken", reRefreshToken);
                  
                  prevRequest.headers.Authorization = reAccessToken;
                  
                  return await axios(prevRequest);
                })
                .catch((e) => {
                  console.log("토큰 재발급 실패");
                  return new Error(e);
                });
            }
            return await issuedToken();
          } else {
            throw new Error("There is no refresh token");
          }
        }
        else if(errResponseStatus === 400) {
          return;
        }
        else if(errResponseStatus === 401) {
          console.log("인증 실패");
          window.location.replace("/login");
        }
        else if(errResponseStatus === 403) {
          alert("권한이 없습니다.");
        }
      } catch (e) {
        return Promise.reject(e);
      }
    }
  );

  const getPetInfo = () => {
    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken")
      }
    }

    const fetchData = async function fetch() {
      const response = await axiosInstance.get(`/api/auth/pet/`, config);
      setText(response.data);
    }

    fetchData();
  }

  useEffect(() => {
  }, [])

  const navigate = useNavigate();
  let detail = text.filter((item) => item.petName === id);
  let gender = detail[0].gender === 'FEMALE' ? true : false;
  let neutered = detail[0].neutered === 'true' ? true : false;
  let surgery = detail[0].surgery[0] === undefined ? false : true;
  
  const now = new Date();
  const start = new Date(detail[0].birthday);

  const timeDiff = now.getTime() - start.getTime();
  const day = Math.floor(timeDiff / (1000*60*60*24)+1);

  const calFood = () => {
    if (day <= 60) return detail[0].weight * 7
    else if(day <= 90) return detail[0].weight * 6
    else if(day <= 150) return detail[0].weight * 5
    else if(day <= 365) return detail[0].weight * 3
    else if(day >= 365 && day <= 1825) return detail[0].weight * 2.5
    else if(day >= 1825) return detail[0].weight * 2
  }

  return (
    <div>
      <Header />
      <CustomBody>
        <div className={styles.container}>
          <div className={styles.info}>
            <img
              className={styles.profileimg}
              src={process.env.PUBLIC_URL + '/img/profile.png'}
              alt="프로필 이미지"
              width="70px"
            />
            <h1>{detail[0].name}</h1>
            <div>
              <h4>품종</h4>
              <span>{detail[0].species}</span>
              <br/>
              <h4>생일</h4>
              <span style={{marginRight: "30px"}}>{detail[0].birthday}</span>
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
              <h4 style={{marginRight: "27px"}}>수술이력</h4>
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
                    <div className={styles.surgery}>
                      <span>{item.surgeryDate}</span>
                      <span>{item.surgeryName}</span>
                    </div>
                  )
                })
                : null}
              <div className={styles.medicine}>
                <h4>복용약</h4>
                <div>
                  {detail[0].medication.map((item) => {
                      return (
                        <div className={styles.medicineinfo}> 
                          <p>{item.medicationName}</p>
                          <span>{item.medicationStart} ~</span>
                          <span>{item.medicationEnd}</span>
                        </div>
                      )
                    })
                  }
                </div>
              </div>
              <h4>주의할 점</h4>
              <p>{detail[0].caution}</p>
            </div>
          </div>
          <div className={styles.weight}>
            <h4 style={{marginRight: "50px"}}>몸무게</h4>
            <span>{detail[0].weight}  kg</span>
          </div>
          <div className={styles.care}>
            <h4>🦴 건강관리</h4>
            <span>권장 사료량</span>
            <span>{calFood()}g</span>
            <br/>
            <span>권장 음수량</span>
            <span>{detail[0].weight*80}ml</span>
            <br/>
            <span>권장 운동량</span>
            <h4>💉 예방접종 이력</h4>
            {detail[0].vaccine.map((item) => {
              return (
                <div className={styles.vaccine}>
                  <span>{item.vaccineDate}</span>
                  <span>{item.vaccineName}</span>
                </div>
              )
            })}
          </div>
        </div>
        <div className={styles.btn}>
          <Button name="삭제하기"/>
          <Button name="수정하기" onClick={() => {navigate(`/updatepet/${id}`)}}/>
        </div>
      </CustomBody>
    </div>
  );
};

export default DetailPet;