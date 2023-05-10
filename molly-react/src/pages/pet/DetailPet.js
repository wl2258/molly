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
  const [text, setText] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken")
      }
    }

    axiosInstance.get(`/api/auth/pet/${id}`, config)
      .then((response) => {
        setText(response.data.data);
        setLoading(false);
      })
      .catch((e) => {
        console.log(e);
      })
  }, [])

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
          console.log(error.response.data.data)
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

  const navigate = useNavigate();

  const now = new Date();
  const start = new Date(text?.birthdate);

  const timeDiff = now.getTime() - start.getTime();
  const day = Math.floor(timeDiff / (1000*60*60*24)+1);

  const calFood = () => {
    if (day <= 60) return text.weight * 7
    else if(day <= 90) return text.weight * 6
    else if(day <= 150) return text.weight * 5
    else if(day <= 365) return text.weight * 3
    else if(day >= 365 && day <= 1825) return text.weight * 2.5
    else if(day >= 1825) return text.weight * 2
  }

  const deletePet = () => {
    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken")
      }
    }

    axiosInstance.delete(`/api/auth/pet/${id}`, config)
      .then((response) => {
        console.log(response);
        console.log("삭제 완료");
      })
      .catch((e) => {
        console.log(e);
      })
  }

  if (loading) {
    return (
      <div>
        <Header />
        <CustomBody><p style={{marginLeft: "100px"}}>loading</p>
        </CustomBody>
      </div>
    )
  }

  if (Object.keys(text).length === 0 ) {
    return null;
  }

  return (
    <div>
      <Header />
      <CustomBody>
        <div className={styles.container}>
          <div className={styles.info}>
            <img
              className={styles.profileimg}
              src={text.profileImage !== null ? text.profileImage : process.env.PUBLIC_URL + '/img/profile.png'}
              alt="프로필 이미지"
              width="70px"
            />
            <h1>{text.petName}</h1>
            <div>
              <h4>품종</h4>
              <span>{text.species}</span>
              <br/>
              <h4>생일</h4>
              <span style={{marginRight: "30px"}}>{text.birthdate}</span>
              <br/>
              <h4>성별</h4>
              <label className={styles.radio}>
                <input type="radio" readOnly={true} value="암컷" checked={text.gender === 'FEMALE' ? true : false}/>
                <span>암컷</span>
                <input type="radio" readOnly={true} value="수컷" checked={text.gender === 'FEMALE' ? false : true}/>
                <span>수컷</span>
              </label>
              <br/>
              <h4>중성화</h4>
              <label className={styles.radio}>
                <input type="radio" readOnly={true} value="함" checked={text.neuteredStatus}/>
                <span>함</span>
                <input type="radio" readOnly={true} value="안 함" checked={!text.neuteredStatus}/>
                <span>안 함</span>
              </label>
              <br/>
              <h4 style={{marginRight: "27px"}}>수술이력</h4>
              <label className={styles.radio}>
                <input type="radio" readOnly={true} value="있음" checked={text.surgery === null ? false : true}/>
                <span>있음</span>
                <input type="radio" readOnly={true} value="없음" checked={text.surgery === null ? true : false}/>
                <span>없음</span>
              </label>
              <br/>
              {text.surgery !== null ? 
                text.surgery.map((item) => {
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
                  {text.medication !== null ? text.medication.map((item) => {
                      return (
                        <div className={styles.medicineinfo}> 
                          <p>{item.medicationName}</p>
                          <span>{item.medicationStartDate} ~</span>
                          <span>{item.medicationEndDate}</span>
                        </div>
                      )
                    })
                  : null}
                </div>
              </div>
              <h4>주의할 점</h4>
              <p>{text.caution}</p>
            </div>
          </div>
          <div className={styles.weight}>
            <h4 style={{marginRight: "50px"}}>몸무게</h4>
            <span>{text.weight}  kg</span>
          </div>
          <div className={styles.care}>
            <h4>🦴 건강관리</h4>
            <span>권장 사료량</span>
            <span>{calFood()}g</span>
            <br/>
            <span>권장 음수량</span>
            <span>{text.weight*80}ml</span>
            <br/>
            <span>권장 운동량</span>
            <h4>💉 예방접종 이력</h4>
            {text.vaccination !== null ? text.vaccination.map((item) => {
              return (
                <div className={styles.vaccine}>
                  <span>{item.vaccinationDate}</span>
                  <span>{item.vaccinationName}</span>
                </div>
              )
            }) : null}
          </div>
        </div>
        <div className={styles.btn}>
          <Button name="삭제하기" onClick={deletePet}/>
          <Button name="수정하기" onClick={() => {navigate(`/updatepet/${id}`)}}/>
        </div>
      </CustomBody>
    </div>
  );
};

export default DetailPet;