import React, { forwardRef, useEffect, useState } from 'react';
import styles from '../../css/Vaccine.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { FiPlus } from 'react-icons/fi';
import { TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import {ko} from 'date-fns/esm/locale';
import VaccineHistory from './VaccineHistory';
import axios from 'axios';
import useDidMountEffect from '../../pages/useDidMountEffect';

const Vaccine = (props) => {
  const [vaccine, setVaccine] = useState(false);
  const [vaccineValue, setVaccineValue] = useState('종합백신 1차');
  const [vaccineDate, setVaccineDate] = useState(new Date());
  const [vaccineHistory, setVaccineHistory] = useState([]);
  const [vaccineNo, setVaccineNo] = useState("");
  const [loading, setLoading] = useState(false);

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

  useState(() => {
    setLoading(true);

    setVaccineHistory([
      {
				"vaccinationId": 1888,
				"vaccinationName": "종합백신1차",
				"vaccinationDate": "2018-01-01"
			}
    ])
  }, [])

  useEffect(() => {
    setVaccineNo(vaccineHistory[vaccineHistory.length - 1].vaccinationId);
    setLoading(false);
}, []);

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

            if (errResponseStatus === 400 && errMsg === "만료된 토큰입니다") {
                const preRefreshToken = localStorage.getItem("refreshToken");
                if (preRefreshToken) {
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
            else if (errResponseStatus === 400) {
                console.log(error.response.data.data)
            }
            else if (errResponseStatus === 401) {
                console.log("인증 실패");
                window.location.replace("/login");
            }
            else if (errResponseStatus === 403) {
                alert("권한이 없습니다.");
            }
        } catch (e) {
            return Promise.reject(e);
        }
    }
  );

  const registerVaccine = () => {
    const config = {
        headers: {
            Authorization: localStorage.getItem("accessToken"),
            "Content-Type": "application/json"
        }
    }

    const data = {
      "vaccinationName": vaccineValue,
      "vaccinationDate": props.dateFormat(vaccineDate)
    }

    const fetchData = async function fetch() {
        const response = await axiosInstance.post(`/api/auth/pet/vaccination`, data, config)
        console.log(response);
        if (response.data.code === 1) {
            let updateVaccine = vaccineHistory;
            updateVaccine.splice(vaccineHistory.length-1, 1, {
                vaccinationId: response.data.data.vaccinationId,
                vaccinationName: vaccineValue,
                vaccinationDate: props.dateFormat(vaccineDate)
            })
            props.setVaccineHistory(updateVaccine)
            console.log("백신 이력 추가 완료");
        }
        else {
            console.log("백신 이력 추가 실패");
        }
    }

    fetchData();
  }

  if (loading) {
    return (
      <div className={styles.vaccine}>
          <div className={styles.modalContainer}>
              <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
              <h1>예방접종 이력 추가</h1>
              <p style={{ marginLeft: "100px" }}>loading</p>
          </div>
      </div>
    )
  }

  if (vaccineHistory.length === 0) {
    return null;
  }

  return (
    <div className={styles.vaccine}>
      <div className={styles.modalContainer}>
        <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px"/></span>
        <h1>예방접종 이력 추가</h1>
        <ul>
          <div onClick={() => {setVaccine(!vaccine)}} className={styles.sort}>
            <span className={styles.default}>{vaccineValue}</span>
            {vaccine ? 
              <span style={{position:"absolute", right: "8px"}}><MdExpandLess size="25px" color="#AFA79F"/></span> : 
              <span style={{position:"absolute", right: "8px"}}><MdExpandMore size="25px" color="#AFA79F"/></span>}
            {vaccine && <VaccineDropdown setValue={setVaccineValue}/>}
          </div>
        </ul>
        <div className={styles.datepicker} onClick={(e) => {e.preventDefault()}}>
          <DatePicker 
            locale={ko}
            selected={vaccineDate}
            onChange={(date) => setVaccineDate(date)}
            dateFormat="yyyy/MM/dd"
            customInput={<CustomInput />}
          />
        </div> 
        <span onClick={() => {
          setVaccineHistory([...vaccineHistory, {
            vaccinationId : parseInt(vaccineNo) + 1,
            vaccinationName : vaccineValue,
            vaccinationDate : props.dateFormat(vaccineDate),
          }])
          setVaccineNo(vaccineHistory[vaccineHistory.length-1].vaccinationId);
          console.log(vaccineHistory)
          registerVaccine();
        }}><FiPlus color="#AFA79F" size="18px"/></span>
        <div>
          {vaccineHistory.map((data, index) => {
            return (
              <VaccineHistory 
                petId={props.petId}
                data={data}
                index={index}
                vaccineHistory={vaccineHistory}
                setVaccineHistory={setVaccineHistory}
                dateFormat={props.dateFormat}
              />
            )
          })}
        </div>
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

const CustomInput = forwardRef(({value, onClick}, ref) => (
  <button className={styles.custominput} onClick={onClick} ref={ref}>
    {value}
  </button>
))

export default Vaccine;