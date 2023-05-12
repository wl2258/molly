import React, { forwardRef, useEffect, useState } from 'react';
import styles from '../../css/Surgery.module.css';
import { FiPlus } from 'react-icons/fi';
import { TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';
import useDidMountEffect from '../../pages/useDidMountEffect';
import SurgeryHistory from './SurgeryHistory';
import axios from 'axios';
import { Button } from '../../components/Button.js';

const Surgery = (props) => {
    const [surgery, setSurgery] = useState(""); // 수술 라디오 버튼
    const [surgeryDate, setSurgeryDate] = useState(new Date());
    const [surgeryName, setSurgeryName] = useState("");
    const [surgeryNo, setSurgeryNo] = useState("");
    const [surgeryHistory, setSurgeryHistory] = useState([]);
    const [loading, setLoading] = useState(false);
    const [confirm, setConfirm] = useState(false);

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

    useEffect(() => {
        setLoading(true);

        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken")
            }
        }

        axiosInstance.get(`/api/auth/pet/surgery/${props.petId}`, config)
            .then((response) => {
                setSurgeryHistory(response.data.data.surgery);
            })
            .catch((e) => {
                console.log(e);
            })
    }, [])
    
    // useEffect(() => {
    //     setLoading(true);

    //     setSurgeryHistory([
	// 		{
	// 			"surgeryId": 1468,
	// 			"surgeryName": "수직이도성형술",
	// 			"surgeryDate": "2023-01-01"
	// 		}
	// 	])
    // }, [])

    useDidMountEffect(() => {
        setSurgery(surgeryHistory.length !== 0 ? "있음" : "없음");
        surgeryHistory.length !== 0 ? setSurgeryNo(surgeryHistory[surgeryHistory.length - 1].surgeryId) :
        setSurgeryNo(1);
        setLoading(false);
    }, [surgeryHistory]);

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

    const handleSurgeryButton = (e) => {
        setSurgery(e.target.value);
    }

    const handleSurgeryFalseButton = (e) => {
        setSurgery(e.target.value);
        setConfirm(true);
    }

    const handleDeleteConfirm = () => {
        setConfirm(!confirm);
        setSurgery("없음")
        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken")
            }
        }

        for(let i=0; i<surgeryHistory.length; i++) {
            const data = {
                "petId": props.petId,
                "surgeryId": surgeryHistory[i].surgeryId
            }

            axiosInstance.delete(`/api/auth/pet/surgery`, data, config)
            .then((response) => {
                console.log(response);
                console.log("모든 수술 이력 삭제");
            });

        }  
    }

    const handleCancleConfirm = () => {
        setConfirm(!confirm);
        setSurgery("있음")
    }

    console.log(surgery)
    
    const registerSurgery = () => {
        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken"),
                "Content-Type": "application/json"
            }
        }

        const data = {
            "surgeryName": surgeryName,
            "surgeryDate": props.dateFormat(surgeryDate)
        }

        const fetchData = async function fetch() {
            const response = await axiosInstance.post(`/api/auth/pet/surgery`, data, config)
            console.log(response);
            if (response.data.code === 1) {
                let updateSurgery = surgeryHistory;
                updateSurgery.splice(surgeryHistory.length-1, 1, {
                    surgeryId: response.data.data.surgeryId,
                    surgeryDate: props.dateFormat(surgeryDate),
                    surgeryName: surgeryName
                })
                props.setSurgeryHistory(updateSurgery)
                console.log("수술이력 추가 완료");
            }
            else {
                console.log("수술이력 추가 실패");
            }
        }

        fetchData();
    }

    if (loading) {
        return (
            <div className={styles.surgeryContainer}>
                <div className={styles.modalContainer}>
                    <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
                    <h1>수술 이력 추가</h1>
                    <p style={{ marginLeft: "100px" }}>loading</p>
                </div>
            </div>
        )
    }

    if (surgeryHistory.length === 0) {
        return null;
    }

    return (
        <div className={styles.surgeryContainer}>
            <div className={styles.modalContainer}>
                <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
                <h1>수술 이력 추가</h1>
                <label className={styles.radio}>
                    <input type="radio" id="true" onChange={handleSurgeryButton} value="있음" checked={surgery === "있음"} />
                    <label htmlFor="true"><span>있음</span></label>
                    <input type="radio" id="false" onChange={handleSurgeryFalseButton} value="없음" checked={surgery === "없음"} />
                    <label htmlFor="false"><span>없음</span></label>
                </label>
                {surgery === "있음" ?
                    <div className={styles.surgery}>
                        <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                            <DatePicker
                                locale={ko}
                                selected={surgeryDate}
                                onChange={(date) => { setSurgeryDate(date) }}
                                dateFormat="yyyy/MM/dd"
                                customInput={<CustomInput />}
                            />
                        </div>
                        <input
                            className={styles.surgeryname}
                            placeholder="수술명"
                            type="text"
                            onChange={(e) => { setSurgeryName(e.target.value) }}
                            value={surgeryName}></input>
                        <span onClick={() => {
                            if (surgeryName !== "")
                                setSurgeryHistory([...surgeryHistory, {
                                    surgeryId: parseInt(surgeryNo) + 1,
                                    surgeryDate: props.dateFormat(surgeryDate),
                                    surgeryName: surgeryName
                                }])
                            setSurgeryNo(surgeryHistory[surgeryHistory.length - 1].surgeryId)
                            setSurgeryName("");
                            registerSurgery();
                        }}><FiPlus color="#AFA79F" size="18px" /></span>
                        <div>
                            {surgeryHistory.map((data, index) => {
                                return (
                                    <SurgeryHistory 
                                        petId={props.petId}
                                        data={data}
                                        index={index}
                                        surgeryHistory={surgeryHistory}
                                        setSurgeryHistory={setSurgeryHistory}
                                        dateFormat={props.dateFormat}
                                    />
                                )
                            })}
                        </div>
                    </div> : confirm && <ConfirmModal onClickDelete={handleDeleteConfirm} onClickCancle={handleCancleConfirm}/>}
            </div>
        </div>
    );
};

const CustomInput = forwardRef(({ value, onClick }, ref) => (
    <button className={styles.custominput} onClick={onClick} ref={ref}>
        {value}
    </button>
))

const ConfirmModal = (props) => {
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
      <div className={styles.confirmModal}>
        <div className={styles.confirm}>
          <div>
            <p>수술 이력이 모두 삭제됩니다.</p>
            <div>
                <Button name="취소" onClick={props.onClickCancle}/>
                <Button name="확인" onClick={props.onClickDelete}/>
            </div>
          </div>
        </div>
      </div>
    )
  }

export default Surgery;